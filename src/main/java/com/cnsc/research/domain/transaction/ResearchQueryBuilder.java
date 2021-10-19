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

     private List<String> agency;
     private List<Double> budget;
     private List<LocalDate> date;
     private List<String> researchers;
     private List<String> status;
     private List<String> unit;
}
