package com.cnsc.research.service;

import com.cnsc.research.domain.exception.InvalidCsvFieldException;
import com.cnsc.research.domain.exception.InvalidResearchStatusException;
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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class ResearchService {
    private final Logger logger;
    private final DeliveryUnitRepository deliveryUnitRepository;
    private final FundingAgencyRepository fundingAgencyRepository;
    private final ResearchersRepository researchersRepository;
    private final ResearchFileRepository researchFileRepository;
    private final ResearchRepository researchRepository;
    private final ResearchStatusRepository researchStatusRepository;


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
                           ResearchStatusRepository researchStatusRepository
    ) {

        this.logger = logger;
        this.deliveryUnitRepository = deliveryUnitRepository;
        this.fundingAgencyRepository = fundingAgencyRepository;
        this.researchersRepository = researchersRepository;
        this.researchFileRepository = researchFileRepository;
        this.researchRepository = researchRepository;
        this.researchStatusRepository = researchStatusRepository;
    }


    /**
     * To save all the data at once.
     *
     * @param multipartFile the multipart file
     * @throws InvalidCsvFieldException the invalid csv field exception
     * @throws CsvException             the csv exception
     * @throws IOException              the io exception
     */
    public void addBatch(MultipartFile multipartFile) throws InvalidCsvFieldException, CsvException, IOException {
        this.multipartFile = multipartFile;
        csv = rewriteFileToLocal();
        List<String[]> rows = csv.getRows();
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


    private static int
            titleIndex,
            researchersIndex,
            unitIndex,
            agencyIndex,
            budgetIndex,
            startIndex,
            endIndex,
            statusIndex,
            remarksIndex;

    private void assignFieldIndices() {
        Map<String, Integer> indices = csv.getRowIndices();
        titleIndex = indices.get(ResearchFields.TITLE.name());
        researchersIndex = indices.get(ResearchFields.RESEARCHERS.name());
        unitIndex = indices.get(ResearchFields.DELIVERY_UNIT.name());
        agencyIndex = indices.get(ResearchFields.FUNDING_AGENCY.name());
        budgetIndex = indices.get(ResearchFields.BUDGET.name());
        startIndex = indices.get(ResearchFields.START_DATE.name());
        endIndex = indices.get(ResearchFields.END_DATE.name());
        statusIndex = indices.get(ResearchFields.STATUS.name());
        remarksIndex = indices.get(ResearchFields.REMARK.name());
    }

    //TODO saving research
    private void saveResearch(List<String[]> rows) {
        /*rows.forEach(row -> {
            Research research = new Research();
            research.setBudget(Double.valueOf(row[budgetIndex]));
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
            research.setStartDate(LocalDate.from(formatter.parse(row[startIndex])));
            //TODO add research
        });*/
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

    private ResearchStatus validateStatus(String statusName, String researchTitle) throws InvalidResearchStatusException {
        Optional<ResearchStatus> researchStatus = researchStatusRepository.findByStatusTypeIgnoreCase(statusName);
        return researchStatus
                .orElseThrow(() -> new InvalidResearchStatusException(researchTitle));
    }


    public List<ResearchDto> getResearchesFromCsv(MultipartFile file) throws IOException, InvalidCsvFieldException, CsvException {
        List<ResearchDto> researches = new ArrayList<>();
        this.multipartFile = file;
        logger.info("File rewritten : "+multipartFile.getOriginalFilename());
        csv = rewriteFileToLocal();
        List<String[]> rows = csv.getRows();
        assignFieldIndices();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        rows.forEach(row -> {
            String[] researchers = row[researchersIndex].split(",");
            List<String> researcherList = new ArrayList<>();
            for (String researcher : researchers) {
                researcherList.add(researcher.trim());
            }
            researches.add(
                    new ResearchDto(
                            null,
                            row[titleIndex],
                            row[agencyIndex],
                            Double.valueOf(row[budgetIndex]),
                            LocalDate.from(formatter.parse(row[startIndex])),
                            LocalDate.from(formatter.parse(row[endIndex])),
                            row[statusIndex],
                            row[unitIndex],
                            row[remarksIndex],
                            researcherList
                    )
            );
        });
        return researches;
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
