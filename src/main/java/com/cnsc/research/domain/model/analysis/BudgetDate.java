package com.cnsc.research.domain.model.analysis;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BudgetDate {
    public Integer date;
    public Double budget;
}
