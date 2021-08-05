package com.cnsc.research.domain.transaction;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResearchDto {

    private Integer id;
    private String researchTitle;
    private String fundingAgency;
    private Double budget;
    private LocalDate startDate;
    private LocalDate endDate;
    private String researchStatus;
    private String DeliveryUnit;
    private String remarks;
    private List<String> researchers;


}
