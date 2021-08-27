package com.cnsc.research.misc;

import com.cnsc.research.configuration.util.SystemContext;
import com.cnsc.research.domain.exception.InvalidCsvFieldException;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.ApplicationContext;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configurable
public class CsvHandler {

    private static final List<List<String>> FIELDS = Arrays.asList(
            Arrays.asList("title","research title","research_title"),                                       // TITLE
            Arrays.asList("researchers","researcher","proponents","research team","research_team"),         // RESEARCHERS
            Arrays.asList("delivery_unit","delivery unit","delivery_team","delivery team", "department"),   // DELIVERY UNIT
            Arrays.asList("funding_agency","funding agency","sponsor","sponsors"),                          // FUNDING AGENCY
            Arrays.asList("budget","amount"),                                                               // BUDGET
            Arrays.asList("start_date","start date","date_started","date started"),                         // DATE STARTED
            Arrays.asList("end_date","end date","date_ended","date end"),                                   // DATE ENDED
            Arrays.asList("status","research_status","research status"),                                    // RESEARCH STATUS
            Arrays.asList("remark","remarks","with moa","with_moa","comment","comments")                    //REMARKS
    );

    private Map<String,Integer> rowIndex;

    private CSVReader reader;

    @Autowired
    private Logger logger ;

    public CsvHandler(File file) throws FileNotFoundException {
        ApplicationContext context = SystemContext.getAppContext();
        logger = context.getBean(Logger.class);
        rowIndex = new HashMap<>();
        reader = new CSVReader(new FileReader(file));
    }


    public List<String []> getRows() throws IOException, CsvException, InvalidCsvFieldException {
        List<String []>  rows = reader.readAll();
        validateField(Arrays.asList(rows.get(0)));
        rows.remove(0);   // The 1st row csv will contain the fields or the column info.
                                // so such row must remove before returning the rows of data
        reader.close();
        return rows;
    }

    private void validateField(List<String> fields) throws InvalidCsvFieldException {
        int validFieldCount = 0;
        for (String field:fields) {
            for (List<String> fieldList : FIELDS) {
                for (String validField : fieldList ) {
                    if (validField.equalsIgnoreCase(field)){
                        logger.info(String.format("validation : %s is equal to %s",field,validField));
                        rowIndex.put(fieldList.get(0).toUpperCase(),validFieldCount);//to identify field arrangement
                        validFieldCount++;
                        break;
                    }
                }
            }
        }
        if (validFieldCount != 9) throw new InvalidCsvFieldException("Please put valid fields on csv file.");
    }


    /**
     * Gets row indices.
     * returns the 1st row of csv that contains the cell identifier or the field
     * @return the row indices
     */
    public Map<String, Integer> getRowIndices() {
        return rowIndex;
    }

}
