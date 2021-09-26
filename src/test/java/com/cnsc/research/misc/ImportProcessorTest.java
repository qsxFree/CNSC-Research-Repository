package com.cnsc.research.misc;

import com.cnsc.research.misc.fields.ResearchFields;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;


class ImportProcessorTest {
    private ImportProcessor processor;

    {
        try {
            processor = new ImportProcessor("C:\\Users\\LC-IPDO-05\\Downloads\\dummy.xlsx", new ResearchFields());
        } catch (IOException | InvalidExcelField | InvalidExcelCellType e) {
            e.printStackTrace();
        }
    }

    @Test
    void fieldLengthShouldEqualToOriginal() {
        int expectedSize = new ResearchFields().getFieldKeys().size();
        int validFieldsLength = processor.getFieldKeys().length;
        assertThat(validFieldsLength).isEqualTo(expectedSize);
    }

    @Test
    void fieldsAreEquals() {
        String[] validatedKeys = processor.getFieldKeys();
        String[] expectedKeys = new ResearchFields().getFieldKeys().toArray(String[]::new);
        assertThat(validatedKeys).contains(expectedKeys);
    }


}
