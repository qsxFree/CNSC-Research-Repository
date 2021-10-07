package com.cnsc.research.domain.mapper;

import java.util.List;
import java.util.Map;

public interface DataImportMapper<D, T> {

    /**
     * Excel to transaction t.
     *
     * @param cellData       the cell data
     * @param keyArrangement the key arrangement
     * @return the t - transaction
     */
    T dataImportToTransaction(String[] cellData, Map<String, Integer> keyArrangement);


    /**
     * Excel to transactions list.
     *
     * @param rowData        the row data
     * @param keyArrangement the key arrangement
     * @return the list
     */
    List<T> dataImportToTransaction(List<String[]> rowData, Map<String, Integer> keyArrangement);

}
