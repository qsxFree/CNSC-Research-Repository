package com.cnsc.research.misc.util;

import com.cnsc.research.configuration.util.SystemContext;
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
public abstract class DataImport {
    private final ApplicationContext context = SystemContext.getAppContext();
    protected Map<String, Integer> keyArrangement;
    protected ValidFields validFields;
    protected Logger logger = context.getBean(Logger.class);

    protected abstract String[] getRawFields() throws Exception;

    public abstract List<String[]> getRawData() throws Exception;

    public abstract void close() throws IOException;

    protected void validateFields() throws Exception {
        keyArrangement = new HashMap<>();// will define the arrangement of fields
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
                keyArrangement.put(validKey.get(), index);
            } else {
                close();
                Optional<String> missingValue = validFields.getFieldKeys()
                        .stream()
                        .filter(field -> keyArrangement.get(field) == null)
                        .findFirst();
                throw new InvalidFieldException(missingValue.orElse(""));
            }
        }
        if (rawFields.length != keyArrangement.size()) throw new InvalidFieldCountException("invalid field size");
        logger.info("Done field validation");
    }


    public Map<String, Integer> getKeyArrangement() {
        return keyArrangement;
    }
}
