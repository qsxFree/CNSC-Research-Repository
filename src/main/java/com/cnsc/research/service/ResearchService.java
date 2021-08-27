package com.cnsc.research.service;

import com.cnsc.research.domain.exception.InvalidCsvFieldException;
import com.cnsc.research.domain.mapper.ResearchMapper;
import com.cnsc.research.domain.model.*;
import com.cnsc.research.domain.repository.*;
import com.cnsc.research.domain.transaction.ResearchDto;
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
import java.util.List;
import java.util.Optional;

@Service
public class ResearchService {
    private final Logger logger;
    private final DeliveryUnitRepository deliveryUnitRepository;
    private final FundingAgencyRepository fundingAgencyRepository;
    private final ResearchersRepository researchersRepository;
    private final ResearchFileRepository researchFileRepository;
    private final ResearchRepository researchRepository;
    private final ResearchMapper researchMapper;

    @Value("${csv-static-directory}")
    private String csvStaticDirectory;

    private MultipartFile multipartFile;
    private CsvHandler csv;


    @Autowired
    public ResearchService(Logger logger,
                           DeliveryUnitRepository deliveryUnitRepository,
                           FundingAgencyRepository fundingAgencyRepository,
                           ResearchersRepository researchersRepository,
                           ResearchFileRepository researchFileRepository,
                           ResearchRepository researchRepository,
                           ResearchMapper researchMapper
    ) {

        this.logger = logger;
        this.deliveryUnitRepository = deliveryUnitRepository;
        this.fundingAgencyRepository = fundingAgencyRepository;
        this.researchersRepository = researchersRepository;
        this.researchFileRepository = researchFileRepository;
        this.researchRepository = researchRepository;
        this.researchMapper = researchMapper;
    }


    private CsvHandler rewriteFileToLocal() throws IOException {
        File file = new File(csvStaticDirectory + multipartFile.getOriginalFilename());//creating directory

        FileOutputStream fileOutputStream = new FileOutputStream(file);

        logger.info("writing file...");

        fileOutputStream.write(multipartFile.getBytes());//rewriting temporary file
        CsvHandler handler = new CsvHandler(file);
        //TODO add delete function after reading data
        return handler;
    }


    //TODO saving research
    public void saveResearches(List<String[]> rows) {
        /*rows.forEach(row -> {
            Research research = new Research();
            research.setBudget(Double.valueOf(row[budgetIndex]));
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
            research.setStartDate(LocalDate.from(formatter.parse(row[startIndex])));
            //TODO add research
        });*/
    }

    public void saveResearch(Research research) {
        //TODO saving individual research
        //TODO Add some validation
    }

    //It will build if and only if there is no data occurrence from database
    private List<Researchers> buildResearchers(String names) {
        List<Researchers> researcherList = new ArrayList<>();
        String[] nameArr = names.split(",");
        for (String name : nameArr) {
            name = name.trim();
            Optional<Researchers> researchers = researchersRepository.findByNameIgnoreCase(name);
            if (researchers.isPresent())
                researcherList.add(researchers.get());
            else
                researcherList.add(Researchers
                        .builder()
                        .name(name)
                        .build()
                );// It will build new Researcher in each non-existing researchers
        }
        return researcherList;
    }

    //It will build if and only if there is no data occurrence from database
    private DeliveryUnit buildDeliveryUnit(String name) {
        Optional<DeliveryUnit> deliveryUnit = deliveryUnitRepository.findByUnitNameIgnoreCase(name);
        return deliveryUnit.orElse(DeliveryUnit
                .builder()
                .unitName(name)
                .build());
    }

    private ResearchFile buildResearchFile(String title, String fileName) {
        return ResearchFile.builder()
                .title(title)
                .fileName(fileName)
                .build();
    }

    //It will build if and only if there is no data occurrence from database
    private FundingAgency buildFundingAgency(String agencyName) {
        Optional<FundingAgency> fundingAgency = fundingAgencyRepository.findByAgencyNameIgnoreCase(agencyName);
        return fundingAgency.orElse(FundingAgency
                .builder()
                .agencyName(agencyName)
                .build()
        );
    }

    //TODO add validate status here

    public List<ResearchDto> getResearchesFromCsv(MultipartFile file) throws IOException, InvalidCsvFieldException, CsvException {
        this.multipartFile = file;
        csv = rewriteFileToLocal();
        return researchMapper.csvToResearchDto(csv.getRows(), csv.getRowIndices());
    }

}
