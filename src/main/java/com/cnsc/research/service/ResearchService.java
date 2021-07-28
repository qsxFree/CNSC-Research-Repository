package com.cnsc.research.service;

import com.cnsc.research.domain.exception.InvalidCsvFieldException;
import com.cnsc.research.misc.CsvHandler;
import com.opencsv.exceptions.CsvException;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Service
public class ResearchService {
    private Logger logger;

    @Value("csv-static-directory")
    private String csvStaticDirectory;

    @Autowired
    public ResearchService(Logger logger){
        this.logger = logger;
    }

    public void addBatch(MultipartFile multipartFile) throws IOException, InvalidCsvFieldException, CsvException {
        File file = new File(csvStaticDirectory+multipartFile.getOriginalFilename());
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        logger.info("writing file");
        fileOutputStream.write(multipartFile.getBytes());
        fileOutputStream.close();
        CsvHandler csvHandler = new CsvHandler(file);
        List<String []> rows = csvHandler.getRows();
        rows.forEach(row -> logger.info(Arrays.asList(row).toString()));
        if (file.delete()) logger.info(String.format("File %s deleted",file.getName()));
    }
}
