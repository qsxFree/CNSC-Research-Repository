package com.cnsc.research.misc.util;

import com.cnsc.research.misc.fields.ValidFields;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ExcelImport extends DataImport {
    private final Workbook workbook;
    private final Sheet sheet;
    private Row firstRow;

    /*
    Holds the key arrangement
    The arrangement of field is important when
    getting the value
    */

    public ExcelImport(byte[] dataStream, ValidFields validFields) throws Exception {
        super.validFields = validFields;
        this.workbook = new XSSFWorkbook(new ByteArrayInputStream(dataStream));
        this.sheet = workbook.getSheetAt(0);
        super.validateFields();
    }

    /*
    Get the topmost row that represents the fields in the excel file.
    The fields are important because it is used to validate cell value.
    */
    protected String[] getRawFields() throws Exception {
        firstRow = sheet.getRow(sheet.getTopRow());

        Iterator<Cell> cellIterator = firstRow.iterator();

        List<String> fields = new ArrayList();

        while (cellIterator.hasNext()) {
            Cell cell = cellIterator.next();
            if (cell.getCellType() == CellType.STRING) fields.add(cell.getStringCellValue().toLowerCase().trim());
            else {
                workbook.close();
                throw new InvalidExcelCellType("Invalid cell type");
            }
        }
        sheet.removeRow(firstRow);//to remove after getting the rawfields
        return fields.toArray(String[]::new);
    }

    @Override
    public List<String[]> getRawData() {
        return null;
    }

    public String[] getFieldKeys() {
        List<String> keys = new ArrayList<>();
        keyArrangement.forEach((k, v) -> keys.add(k));
        return keys.toArray(String[]::new);
    }

    public void close() throws IOException {
        this.workbook.close();
    }


}
