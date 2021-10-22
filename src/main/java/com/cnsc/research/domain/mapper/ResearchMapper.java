package com.cnsc.research.domain.mapper;

import com.cnsc.research.domain.model.*;
import com.cnsc.research.domain.transaction.ResearchDto;
import com.cnsc.research.misc.fields.ResearchFields;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * The type Research mapper.
 */
@Component
public class ResearchMapper extends GeneralMapper<Research, ResearchDto> implements DataImportMapper<Research, ResearchDto> {

    private final DateTimeFormatter formatter;

    /**
     * Instantiates a new Research mapper.
     *
     * @param formatter the formatter
     */
    @Autowired
    public ResearchMapper(DateTimeFormatter formatter) {
        this.formatter = formatter;
    }

    @Override
    public ResearchDto toTransaction(Research research) {
        int id = research.getResearchId();
        double budget = research.getBudget();
        LocalDate startDate = research.getStartDate();
        LocalDate endDate = research.getEndDate();
        String remarks = research.getRemarks();
        String researchTitle = research.getResearchFile().getTitle();
        String researchStatus = research.getResearchStatus().name();

        List<ResearchDto.FundingAgency> agencies = research.getFundingAgencies().stream()
                .map(data -> ResearchDto
                        .FundingAgency
                        .builder()
                        .agencyId(data.getAgencyId())
                        .agencyName(data.getAgencyName()).build())
                .collect(Collectors.toList());

        ResearchDto.DeliveryUnit deliveryUnit = ResearchDto.DeliveryUnit.builder()
                .unitId(research.getDeliveryUnit().getUnitId())
                .unitName(research.getDeliveryUnit().getUnitName())
                .build();

        List<ResearchDto.Researchers> researchers = research.getResearchers().stream()
                .map(data -> ResearchDto
                        .Researchers
                        .builder()
                        .researcherId(data.getResearcherId())
                        .name(data.getName()).build())
                .collect(Collectors.toList());

        ResearchDto.ResearchFile researchFile = ResearchDto.ResearchFile.builder()
                .fileId(research.getResearchFile().getFileId())
                .title(research.getResearchFile().getTitle())
                .fileName(research.getResearchFile().getFileName())
                .build();

        return new ResearchDto(
                id,
                agencies,
                budget,
                startDate,
                endDate,
                researchStatus,
                deliveryUnit,
                remarks,
                researchers,
                researchFile
        );
    }

    @Override
    public Research toDomain(ResearchDto researchDto) {
        Research research = new Research();

        research.setResearchId(researchDto.getId());
        research.setBudget(researchDto.getBudget());
        research.setStartDate(researchDto.getStartDate());
        research.setEndDate(researchDto.getEndDate());
        research.setRemarks(researchDto.getRemarks());
        research.setDeleted(false);


        switch (researchDto.getResearchStatus().toLowerCase()) {
            case "new":
                research.setResearchStatus(ResearchStatus.NEW);
                break;
            case "approved":
                research.setResearchStatus(ResearchStatus.APPROVED);
                break;
            case "completed":
                research.setResearchStatus(ResearchStatus.COMPLETED);
                break;
        }

        research.setFundingAgencies(researchDto.getFundingAgency()
                .stream()
                .map(data ->
                        FundingAgency.builder()
                                .agencyId(data.getAgencyId())
                                .agencyName(data.getAgencyName())
                                .build())
                .collect(Collectors.toList())
        );

        research.setDeliveryUnit(DeliveryUnit.builder()
                .unitId(researchDto.getDeliveryUnit().getUnitId())
                .unitName(researchDto.getDeliveryUnit().getUnitName())
                .build());

        research.setResearchers(researchDto.getResearchers()
                .stream()
                .map(data ->
                        Researchers.builder()
                                .researcherId(data.getResearcherId())
                                .name(data.getName())
                                .build())
                .collect(Collectors.toList())
        );
        research.setResearchFile(researchDto.getResearchFile() != null //if researchFile is not null
                ? ResearchFile.builder()
                .fileId(researchDto.getResearchFile().getFileId())
                .fileName(researchDto.getResearchFile().getFileName())
                .title(researchDto.getResearchFile().getTitle())
                .build()
                : ResearchFile.builder().title(researchDto.getResearchFile().getTitle()).build()
        );

        return research;
    }

    @Override
    public ResearchDto dataImportToTransaction(String[] csvRow, Map<String, Integer> keyArrangement) {
        String[] researchers = csvRow[keyArrangement.get(ResearchFields.RESEARCHER_KEY)].split(",");
        String[] fundingAgency = csvRow[keyArrangement.get(ResearchFields.FUNDING_AGENCY_KEY)].split(",");

        List<ResearchDto.Researchers> researcherList = Arrays.stream(researchers)
                .map(data ->
                        ResearchDto.Researchers.builder()
                                .name(data.trim())
                                .build()
                )
                .collect(Collectors.toList());

        List<ResearchDto.FundingAgency> fundingAgencies = Arrays.stream(fundingAgency)
                .map(data ->
                        ResearchDto.FundingAgency.builder()
                                .agencyName(data)
                                .build()
                )
                .collect(Collectors.toList());


        return new ResearchDto(
                null,
                fundingAgencies,
                Double.valueOf(csvRow[keyArrangement.get(ResearchFields.BUDGET_KEY)]),
                LocalDate.from(formatter.parse(csvRow[keyArrangement.get(ResearchFields.START_DATE_KEY)])),
                LocalDate.from(formatter.parse(csvRow[keyArrangement.get(ResearchFields.END_DATE_KEY)])),
                csvRow[keyArrangement.get(ResearchFields.RESEARCH_STATUS_KEY)],
                ResearchDto.DeliveryUnit.builder().unitName(csvRow[keyArrangement.get(ResearchFields.DELIVERY_UNIT_KEY)]).build(),
                csvRow[keyArrangement.get(ResearchFields.REMARK_KEY)],
                researcherList,
                ResearchDto.ResearchFile.builder().title(csvRow[keyArrangement.get(ResearchFields.TITLE_KEY)]).build()
        );

    }

    @Override
    public List<ResearchDto> dataImportToTransaction(List<String[]> rowData, Map<String, Integer> keyArrangement) {
        return rowData.stream()
                .map(csvRow -> this.dataImportToTransaction(csvRow, keyArrangement))
                .collect(Collectors.toList());
    }


}
