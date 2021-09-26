package com.cnsc.research.domain.mapper;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class ExcelMapper<D, T> extends GeneralMapper<D, T> {

    /**
     * Excel to transaction t.
     *
     * @param cellData       the cell data
     * @param keyArrangement the key arrangement
     * @return the t - transaction
     */
    public abstract T excelToTransaction(String[] cellData, Map<String, Integer> keyArrangement);


    /**
     * Excel to transactions list.
     *
     * @param rowData        the row data
     * @param keyArrangement the key arrangement
     * @return the list
     */
    public List<T> excelToTransactions(List<String[]> rowData, Map<String, Integer> keyArrangement) {
        return rowData.stream()
                .map(csvRow -> this.excelToTransaction(csvRow, keyArrangement))
                .collect(Collectors.toList());
    }

}
