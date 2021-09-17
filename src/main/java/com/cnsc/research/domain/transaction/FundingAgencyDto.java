package com.cnsc.research.domain.transaction;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FundingAgencyDto {
    private Integer agencyId;
    private String agencyName;
}
