package com.cnsc.research.api;

import com.cnsc.research.domain.exception.InvalidCsvFieldException;
import com.cnsc.research.service.ResearchService;
import com.opencsv.exceptions.CsvException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/research")
public class ResearchController {

    private final ResearchService service;

    @Autowired
    public ResearchController(ResearchService service){
        this.service = service;
    }

    @PostMapping("/upload")
    public String uploadCsv(@RequestParam("file") MultipartFile file) throws InvalidCsvFieldException, IOException, CsvException {
        service.addBatch(file);
        return String.format("File Uploaded \nDirectory :%s",file.getOriginalFilename());
    }
}
