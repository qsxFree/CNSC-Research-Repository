package com.cnsc.research.misc;

import com.cnsc.research.configuration.util.SystemContext;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static java.lang.String.format;

@Configurable
public class UploadHandler {
    private final String cacheDirectory;

    private File cachedFile = null;

    @Value("${static-directory}")
    private String staticDirectory;

    @Autowired
    private Logger logger;

    public UploadHandler(String cacheKey) {
        ApplicationContext context = SystemContext.getAppContext();
        logger = context.getBean(Logger.class);
        this.cacheDirectory = staticDirectory + "cache/" + cacheKey + "/";
    }


    private void createDirectory() {
        File directory = new File(cacheDirectory);
        if (!directory.exists()) {
            directory.mkdir();
            logger.info(format("%s created", cacheDirectory));
        } else logger.info(format("%s already exist"));
    }

    public File process(MultipartFile incomingFile) throws IOException {
        return this.internalProcessor(incomingFile,incomingFile.getOriginalFilename());
    }

    private File internalProcessor(MultipartFile incomingFile, String customName) throws IOException {
        createDirectory();
        this.cachedFile = new File(cacheDirectory + customName);
        FileOutputStream fileOutputStream;
        fileOutputStream = new FileOutputStream(cachedFile);
        fileOutputStream.write(incomingFile.getBytes());
        fileOutputStream.close();
        fileOutputStream.flush();
        return cachedFile;
    }

    public boolean deleteCachedFile() {
        if (cachedFile != null) {
            logger.warn(format("%s deleting...", cachedFile.getName()));
            return cachedFile.delete();
        }
        return false;
    }

    private File processWithValidate(MultipartFile incomingFile,String fileExtension) throws IOException {
        String fileName = incomingFile
                .getOriginalFilename()
                .replaceAll(" ", "-")
                .replaceAll("[./\\:?*\"|]", "");

        return this.internalProcessor(incomingFile,fileName + fileExtension);
    }

}
