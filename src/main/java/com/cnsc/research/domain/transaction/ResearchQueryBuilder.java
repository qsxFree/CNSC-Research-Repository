package com.cnsc.research.domain.transaction;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResearchQueryBuilder {

     private List<String> agency = null;
     private List<Double> budget = null;
     private List<LocalDate> date = null;
     private List<String> researchers = null;
     private List<String> status = null;
     private List<String> unit = null;
}
