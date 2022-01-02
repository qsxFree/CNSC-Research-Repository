package com.cnsc.research.domain.model.analysis;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResearcherCount {
    private String researcherName;
    private Integer count;
}
