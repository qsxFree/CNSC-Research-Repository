package com.cnsc.research.misc.importer;

import com.cnsc.research.configuration.util.SystemContext;
import com.cnsc.research.domain.mapper.DataImportMapper;
import com.cnsc.research.domain.transaction.Mappable;
import com.cnsc.research.misc.fields.ValidFields;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.ApplicationContext;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Configurable
public abstract class DataImport<T extends Mappable> {
    private final ApplicationContext context = SystemContext.getAppContext();
    protected Map<String, Integer> keyArrangement;
    protected ValidFields validFields;
    protected Logger logger = context.getBean(Logger.class);

    protected abstract String[] getRawFields() throws Exception;

    public abstract List<String[]> getRawData() throws Exception;

    public abstract void close() throws IOException;

    protected void validateFields() throws Exception {
        Map<String, Integer> tempKeyArrangementAllocator = new HashMap<>();// will define the arrangement of fields
        /*
          Iterates the raw fields.
          it is important to put this in a standard loop syntax to identify the position
          of the field in Excel file.
         */
        logger.info("Validating fields ...");
        String[] rawFields = getRawFields();
        for (int index = 0; index < rawFields.length; index++) {
            String currentField = rawFields[index];
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
                tempKeyArrangementAllocator.put(validKey.get(), index);
            } else {
                close();
                Optional<String> missingValue = validFields.getFieldKeys()
                        .stream()
                        .filter(field -> tempKeyArrangementAllocator.get(field) == null)
                        .findFirst();
                throw new InvalidFieldException(missingValue.orElse(""));
            }
        }

        keyArrangement = Map.copyOf(tempKeyArrangementAllocator);

        if (rawFields.length != keyArrangement.size()) {
            close();
            throw new InvalidFieldCountException("invalid field size");
        }
        logger.info("Done field validation");
    }


    /**
     * Gets row indices.
     * returns the 1st row of csv that contains the cell identifier or the field
     *
     * @return the row indices
     */
    public Map<String, Integer> getKeyArrangement() {
        return keyArrangement;
    }


    public List<T> getMappedData(DataImportMapper<?, T> mapper) throws Exception {
        return mapper.dataImportToTransaction(getRawData(), getKeyArrangement());
    }
}
