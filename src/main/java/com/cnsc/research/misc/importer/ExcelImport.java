package com.cnsc.research.misc.importer;

import com.cnsc.research.domain.transaction.Mappable;
import com.cnsc.research.misc.fields.ValidFields;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ExcelImport<T extends Mappable> extends DataImport<T> {
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
    @Override
    protected String[] getRawFields() throws Exception {
        firstRow = sheet.getRow(sheet.getTopRow());

        Iterator<Cell> cellIterator = firstRow.iterator();

        List<String> fields = new ArrayList();

        while (cellIterator.hasNext()) {
            Cell cell = cellIterator.next();
            if (cell.getCellType() == CellType.STRING) fields.add(cell.getStringCellValue().toLowerCase().trim());
            else if (cell.getCellType() == CellType.BLANK) break;
            else {
                logger.error("Field Validation failed");
                close();
                throw new InvalidExcelCellType("Invalid cell type");
            }
        }
        sheet.removeRow(firstRow);//to remove after getting the rawfields
        return fields.toArray(String[]::new);
    }

    @Override
    public List<String[]> getRawData() {
        List<String[]> result = new ArrayList<>();
        Iterator<Row> rowIterator = sheet.rowIterator();
        rowIterator.forEachRemaining((row) -> {
            Iterator<Cell> cellIterator = row.cellIterator();
            List<String> rowValues = new ArrayList<>();
            cellIterator.forEachRemaining((cell -> {
                switch (cell.getCellType()) {
                    case STRING:
                        rowValues.add(cell.getStringCellValue());
                        break;
                    case NUMERIC:
                        rowValues.add(String.valueOf(cell.getNumericCellValue()));
                        break;
                    case BOOLEAN:
                        rowValues.add(String.valueOf(cell.getBooleanCellValue()));
                        break;
                    default:
                        break;
                }
            }));
            if (rowValues.size() > 0)
                result.add(rowValues.toArray(new String[0]));
        });
        return result;
    }

    @Override
    public void close() throws IOException {
        this.workbook.close();
    }


}
