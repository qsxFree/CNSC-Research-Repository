package com.cnsc.research.misc.storage;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class LocalStorageUtility extends StorageUtility {
    public static final String PDF_CONTAINER = "pdf";
    public static final String DOCUMENT_CONTAINER = "documents";
    private final String PATH = "D:\\Projects\\React\\CNSC-Research-Repository-FrontEnd\\src\\";
    private String container;

    private Path filePath;

    public LocalStorageUtility() {
    }

    @Override
    public void upload(byte[] streamData, String fileName) throws IOException {
        this.filePath = Paths.get(container, fileName);
        File file = new File(filePath.toFile().getParentFile(), fileName);
        System.out.println(file.getAbsolutePath());
        file.createNewFile();
        try (OutputStream os = Files.newOutputStream(this.filePath)) {
            os.write(streamData);
        }
    }


    public void upload(MultipartFile file, String fileName) throws IOException {
        this.filePath = Paths.get(container, fileName);
        file.transferTo(this.filePath);
    }

    @Override
    public void uploadOverwrite(byte[] streamData, String fileName) throws IOException {

    }

    @Override
    public void delete(String filename) {

    }

    @Override
    public StorageUtility inContainer(String container) {
        this.container = PATH + container + "/";
        return this;
    }
}
