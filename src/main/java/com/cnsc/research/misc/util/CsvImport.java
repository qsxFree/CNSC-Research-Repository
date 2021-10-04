package com.cnsc.research.misc.util;

import com.cnsc.research.misc.fields.ValidFields;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

public class CsvImport extends DataImport {

    private final CSVReader reader;
    private String[] firstRow;


    public CsvImport(byte[] dataStream, ValidFields validFields) throws Exception {
        super.validFields = validFields;
        reader = new CSVReader(new InputStreamReader(new ByteArrayInputStream(dataStream)));
        validateFields();
    }

    public List<String[]> getRawData() throws IOException, CsvException {
        List<String[]> rows = reader.readAll();
        rows.remove(0);   // The 1st row csv will contain the fields or the column info.
        // so such row must remove before returning the rows of data
        return rows;
    }

    protected String[] getRawFields() throws Exception {
        this.firstRow = reader.peek();
        return this.firstRow;
    }


    /**
     * Gets row indices.
     * returns the 1st row of csv that contains the cell identifier or the field
     *
     * @return the row indices
     */
    @Override
    public Map<String, Integer> getKeyArrangement() {
        return super.keyArrangement;
    }


    @Override
    public void close() throws IOException {
        reader.close();
    }
}
