package com.cnsc.research.misc;

import com.cnsc.research.misc.fields.ValidFields;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class ImportProcessor {
    private final FileInputStream fileInputStream;
    private final Workbook workbook;
    private final Sheet sheet;
    private final ValidFields validFields;
    private Row firstRow;
    /*
    Holds the key arrangement
    The arrangement of field is important when
    getting the value
    */
    private Map<String, Integer> keyArrangement;

    public ImportProcessor(File file, ValidFields validFields) throws IOException, InvalidExcelCellType, InvalidExcelField {
        this.fileInputStream = new FileInputStream(file);
        this.workbook = new XSSFWorkbook(fileInputStream);
        this.sheet = workbook.getSheetAt(0);
        this.validFields = validFields;
        validateFields();
    }


    /*
    Get the topmost row that represents the fields in the excel file.
    The fields are important because it is used to validate cell value.
    */
    private String[] getRawFields() throws InvalidExcelCellType {
        firstRow = sheet.getRow(sheet.getTopRow());

        Iterator<Cell> cellIterator = firstRow.iterator();

        List<String> fields = new ArrayList();

        while (cellIterator.hasNext()) {
            Cell cell = cellIterator.next();
            if (cell.getCellType() == CellType.STRING) fields.add(cell.getStringCellValue().toLowerCase().trim());
            else throw new InvalidExcelCellType("Invalid cell type");
        }

        return fields.toArray(String[]::new);
    }

    //Validates the field by the given ValidField implementation.
    private void validateFields() throws InvalidExcelCellType, InvalidExcelField {
        keyArrangement = new HashMap<>();// will define the arrangement of fields

        /*
         * Iterates the raw fields.
         * it is important to put this in a standard loop syntax to identify the position
         * of the field in Excel file.
         * */
        for (int index = 0; index < getRawFields().length; index++) {
            String currentField = getRawFields()[index];
            AtomicReference<String> validKey = new AtomicReference<>("");
            boolean valueExist = validFields.getFieldKeys()
                    .stream()
                    .anyMatch(fieldKey -> {
                        validKey.set(fieldKey);
                        return validFields.getFieldValues(fieldKey)
                                .stream()
                                .anyMatch(field -> field.equalsIgnoreCase(currentField));
                    });

            if (valueExist) {
                keyArrangement.put(validKey.get(), index);
            } else {
                throw new InvalidExcelField("Invalid excel field named " + currentField);
            }
        }
    }


    public Map<String, Integer> getKeyArrangement() {
        return keyArrangement;
    }

    public String[] getFieldKeys() {
        List<String> keys = new ArrayList<>();
        keyArrangement.forEach((k, v) -> keys.add(k));
        return keys.toArray(String[]::new);
    }
}
