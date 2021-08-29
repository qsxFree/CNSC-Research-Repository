package com.cnsc.research.domain.transaction;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResearchBatchQueryResponse {
    private List<ResearchDto> result;
    private int nextPage;
    private int prevPage;
    private int totalPage;
    private long totalElements;
}
