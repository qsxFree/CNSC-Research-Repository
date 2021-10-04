package com.cnsc.research.misc.importer;

import com.cnsc.research.domain.transaction.Mappable;
import com.cnsc.research.misc.fields.ValidFields;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class CsvImport<T extends Mappable> extends DataImport<T> {

    private final CSVReader reader;
    private String[] firstRow;

    public CsvImport(byte[] dataStream, ValidFields validFields) throws Exception {
        super.validFields = validFields;
        reader = new CSVReader(new InputStreamReader(new ByteArrayInputStream(dataStream)));
        validateFields();
    }

    @Override
    public List<String[]> getRawData() throws IOException, CsvException {
        List<String[]> rows = reader.readAll();
        rows.remove(0);   // The 1st row csv will contain the fields or the column info.
        // so such row must remove before returning the rows of data
        return rows;
    }

    @Override
    protected String[] getRawFields() throws Exception {
        this.firstRow = reader.peek();
        return this.firstRow;
    }


    @Override
    public void close() throws IOException {
        reader.close();
    }
}
