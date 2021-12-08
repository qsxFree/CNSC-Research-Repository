package com.cnsc.research.domain.report;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ResearchReport {
    private String title;
    private String agenda;
    private String researcher;
    private String deliveryUnit;
    private String fundingAgency;
    private Double budget;
    private String status;
    private String remarks;
}
