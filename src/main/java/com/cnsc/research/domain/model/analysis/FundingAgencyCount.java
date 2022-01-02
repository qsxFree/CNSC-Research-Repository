package com.cnsc.research.domain.model.analysis;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FundingAgencyCount {
    private String fundingAgency;
    private Integer count;

}
