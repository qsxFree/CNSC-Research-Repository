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

        List<String> fundingAgencies = research.getFundingAgencies()
                .stream()
                .map(FundingAgency::getAgencyName)
                .collect(Collectors.toList());

        String deliveryUnit = research.getDeliveryUnit().getUnitName();
        String researchStatus = research.getResearchStatus().name();
        List<String> researchers = research.getResearchers()
                .stream()
                .map(Researchers::getName)
                //.distinct()
                .collect(Collectors.toList());

        String researchFile = research.getResearchFile().getFileName();

        return new ResearchDto(
                id,
                researchTitle,
                fundingAgencies,
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
        research.setDeleted((byte) 0);


        research.setFundingAgencies(researchDto.getFundingAgency().stream()
                .map(name -> {
                    FundingAgency agency = new FundingAgency();
                    agency.setAgencyName(name);
                    return agency;
                })
                .collect(Collectors.toList())
        );

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

        DeliveryUnit unit = new DeliveryUnit();
        unit.setUnitName(researchDto.getDeliveryUnit());
        research.setDeliveryUnit(unit);

        research.setResearchers(researchDto.getResearchers().stream()
                .map(name -> {
                    Researchers researcher = new Researchers();
                    researcher.setName(name);
                    return researcher;
                })
                .collect(Collectors.toList())
        );

        ResearchFile researchFile = new ResearchFile();
        researchFile.setTitle(researchDto.getResearchTitle());
        researchFile.setFileName(researchDto.getResearchFile());
        research.setResearchFile(researchFile);

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

        List<String> researcherList = Arrays.stream(researchers)
                .map(String::trim)
                //.distinct()
                .collect(Collectors.toList());

        List<String> fundingAgencies = Arrays.stream(fundingAgency)
                .map(String::trim)
                //.distinct()
                .collect(Collectors.toList());

        return new ResearchDto(
                null,
                csvRow[indices.get("TITLE")],
                fundingAgencies,
                Double.valueOf(csvRow[indices.get("BUDGET")]),
                LocalDate.from(formatter.parse(csvRow[indices.get("START_DATE")])),
                LocalDate.from(formatter.parse(csvRow[indices.get("END_DATE")])),
                csvRow[indices.get("STATUS")],
                csvRow[indices.get("DELIVERY_UNIT")],
                csvRow[indices.get("REMARK")],
                researcherList,
                null // this is null because it cannot include the pdf file when it is uploaded
        );

    }


}
