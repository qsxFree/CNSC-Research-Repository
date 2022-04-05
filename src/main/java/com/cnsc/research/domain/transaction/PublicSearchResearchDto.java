package com.cnsc.research.domain.transaction;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PublicSearchResearchDto {
    private Integer deliveryUnit = null;
    private Integer fundingAgency = null;
    private String researchAgenda = null;
    private Integer researcher = null;
    private String title = null;
}
