package com.cnsc.research.domain.mapper;

import com.cnsc.research.domain.model.*;
import com.cnsc.research.domain.transaction.ResearchDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * The type Research mapper.
 */
@Component
public class ResearchMapper {

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

    /**
     * Mapping for Research to ResearchDTO
     *
     * @param research the research
     * @return the research dto
     */
    public ResearchDto toResearchDto(Research research) {
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

    /**
     * Mapping for Research to ResearchDTO
     *
     * @param data the data
     * @return the list
     */
    public List<ResearchDto> toResearchDto(Collection<Research> data) {
        return data.stream()
                .map(this::toResearchDto)
                .collect(Collectors.toList());
    }


    /**
     * Mapping for ResearchDTO To Research.
     *
     * @param researchDto the research dto
     * @return the research
     */
    public Research toResearch(ResearchDto researchDto) {
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

    /**
     * Mapping for ResearchDTO To Research.
     *
     * @param data the data
     * @return the list
     */
    public List<Research> toResearch(Collection<ResearchDto> data) {
        return data.stream()
                .map(this::toResearch)
                .collect(Collectors.toList());
    }


    /**
     * A special mapping from CSV data to ResearchDto
     *
     * @param csvRows the csv rows
     * @param indices the indices
     * @return the list
     */
    public List<ResearchDto> csvToResearchDto(List<String[]> csvRows, Map<String, Integer> indices) {
        return csvRows.stream()
                .map(csvRow -> this.csvToResearchDto(csvRow, indices))
                .collect(Collectors.toList());
    }

    /**
     * A special mapping from CSV data to ResearchDto
     *
     * @param csvRow  the csv row
     * @param indices the indices
     * @return the research dto
     */
    public ResearchDto csvToResearchDto(String[] csvRow, Map<String, Integer> indices) {
        String[] researchers = csvRow[indices.get("RESEARCHERS")].split(",");
        String[] fundingAgency = csvRow[indices.get("FUNDING_AGENCY")].split(",");

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
                Double.valueOf(csvRow[indices.get("BUDGET")]),
                LocalDate.from(formatter.parse(csvRow[indices.get("START_DATE")])),
                LocalDate.from(formatter.parse(csvRow[indices.get("END_DATE")])),
                csvRow[indices.get("STATUS")],
                ResearchDto.DeliveryUnit.builder().unitName(csvRow[indices.get("DELIVERY_UNIT")]).build(),
                csvRow[indices.get("REMARK")],
                researcherList,
                ResearchDto.ResearchFile.builder().title(csvRow[indices.get("TITLE")]).build()
        );

    }


}
