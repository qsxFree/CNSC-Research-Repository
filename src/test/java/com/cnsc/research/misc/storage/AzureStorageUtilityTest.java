package com.cnsc.research.misc.storage;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

class AzureStorageUtilityTest {
    AzureStorageUtility azureStorageUtility = new AzureStorageUtility();

    @Test
    void shouldAddFile() throws IOException {
        azureStorageUtility.inContainer("pdf").uploadOverwrite("Hello world2".getBytes(StandardCharsets.UTF_8), "" + System.currentTimeMillis());
    }

    @Test
    void shouldDeleteFile() {
        azureStorageUtility.inContainer("pdf").delete("eey.txt");
    }

}
