package com.cnsc.research.service;

import com.cnsc.research.domain.exception.InvalidCsvFieldException;
import com.cnsc.research.domain.model.Researchers;
import com.cnsc.research.domain.repository.*;
import com.cnsc.research.misc.CsvHandler;
import com.opencsv.exceptions.CsvException;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ResearchService {
    private final Logger logger;
    private final DeliveryUnitRepository deliveryUnitRepository;
    private final FundingAgencyRepository fundingAgencyRepository;
    private final ResearchersRepository researchersRepository;
    private final ResearchFileRepository researchFileRepository;
    private final ResearchRepository researchRepository;

    @Value("csv-static-directory")
    private String csvStaticDirectory;
    private MultipartFile multipartFile;

    @Autowired
    public ResearchService(Logger logger,
                           DeliveryUnitRepository deliveryUnitRepository,
                           FundingAgencyRepository fundingAgencyRepository,
                           ResearchersRepository researchersRepository,
                           ResearchFileRepository researchFileRepository,
                           ResearchRepository researchRepository
    ) {

        this.logger = logger;
        this.deliveryUnitRepository = deliveryUnitRepository;
        this.fundingAgencyRepository = fundingAgencyRepository;
        this.researchersRepository = researchersRepository;
        this.researchFileRepository = researchFileRepository;
        this.researchRepository = researchRepository;
    }


    private CsvHandler csv;

    public void addBatch(MultipartFile multipartFile) throws InvalidCsvFieldException, CsvException, IOException {
        this.multipartFile = multipartFile;
        csv = rewriteFileToLocal();
        List<String[]> rows = csv.getRows();
        saveResearchersIfNotExist(rows);
    }

    private void saveResearchersIfNotExist(List<String[]> rows) {
        List<Researchers> researcherList = new ArrayList<>();
        Set<String> nameSet = new HashSet<>();//For filtering name before database filtering

        int researcherKeyIndex = csv.getRowIndices().get(ResearchFields.RESEARCHERS.name());

        rows.forEach(row -> {
            String researchersName[] = row[researcherKeyIndex].split(",");
            for (String name : researchersName) {
                name = name.trim();
                nameSet.add(name);

            }
        });

        nameSet.forEach(name -> {
            if (researchersRepository.findByNameIgnoreCase(name).isEmpty()) {
                Researchers researcher = new Researchers();
                researcher.setName(name);
                researcherList.add(researcher);
            }
        }); // database filtering


        logger.info("Saving researchers...");
        researchersRepository.saveAll(researcherList);

    }

    private CsvHandler rewriteFileToLocal() throws IOException {
        File file = new File(csvStaticDirectory + multipartFile.getOriginalFilename());//creating directory
        try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
            logger.info("writing file...");
            fileOutputStream.write(multipartFile.getBytes());//rewriting temporary file
            return new CsvHandler(file);
        } catch (IOException e) {
            throw new IOException();
        }
    }


    private enum ResearchFields {
        TITLE,
        RESEARCHERS,
        DELIVERY_UNIT,
        FUNDING_AGENCY,
        BUDGET,
        START_DATE,
        END_DATE,
        STATUS,
        REMARK;

        @Override
        public String toString() {
            return this.name();
        }
    }
}
