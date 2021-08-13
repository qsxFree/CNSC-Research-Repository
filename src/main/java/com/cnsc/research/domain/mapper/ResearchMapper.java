package com.cnsc.research.domain.mapper;

import com.cnsc.research.domain.model.Research;
import com.cnsc.research.domain.model.Researchers;
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

@Component
public class ResearchMapper {

    private final DateTimeFormatter formatter;

    @Autowired
    public ResearchMapper(DateTimeFormatter formatter) {
        this.formatter = formatter;
    }

    public ResearchDto toResearchDto(Research research) {
        int id = research.getResearchId();
        double budget = research.getBudget();
        LocalDate startDate = research.getStartDate();
        LocalDate endDate = research.getEndDate();
        String remarks = research.getRemarks();
        String researchTitle = research.getFileIdByResearchFile().getTitle();
        String fundingAgency = research.getFundingAgencyByFundingAgency().getAgencyName();
        String deliveryUnit = research.getDeliveryUnitByDeliveryUnit().getUnitName();
        String researchStatus = research.getResearchStatusByResearchStatus().getStatusType();
        List<String> researchers = research.getResearchers()
                .stream()
                .map(Researchers::getName)
                //.distinct()
                .collect(Collectors.toList());

        return new ResearchDto(
                id,
                researchTitle,
                fundingAgency,
                budget,
                startDate,
                endDate,
                researchStatus,
                deliveryUnit,
                remarks,
                researchers
        );
    }

    public List<ResearchDto> toResearchDto(Collection<Research> data) {
        return data.stream()
                .map(this::toResearchDto)
                .collect(Collectors.toList());
    }

    //TODO add reverse mapping


    /**
     * A special mapping from CSV data to ResearchDto
     */
    public List<ResearchDto> csvToResearchDto(List<String[]> csvRows, Map<String, Integer> indices) {
        return csvRows.stream()
                .map(csvRow -> this.csvToResearchDto(csvRow, indices))
                .collect(Collectors.toList());
    }

    public ResearchDto csvToResearchDto(String[] csvRow, Map<String, Integer> indices) {
        String[] researchers = csvRow[indices.get("RESEARCHERS")].split(",");

        List<String> researcherList = Arrays.stream(researchers)
                .map(String::trim)
                //.distinct()
                .collect(Collectors.toList());

        return new ResearchDto(
                null,
                csvRow[indices.get("TITLE")],
                csvRow[indices.get("FUNDING_AGENCY")],
                Double.valueOf(csvRow[indices.get("BUDGET")]),
                LocalDate.from(formatter.parse(csvRow[indices.get("START_DATE")])),
                LocalDate.from(formatter.parse(csvRow[indices.get("END_DATE")])),
                csvRow[indices.get("STATUS")],
                csvRow[indices.get("DELIVERY_UNIT")],
                csvRow[indices.get("REMARK")],
                researcherList
        );

    }


}
