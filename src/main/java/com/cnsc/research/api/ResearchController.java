package com.cnsc.research.api;

import com.cnsc.research.domain.exception.InvalidCsvFieldException;
import com.cnsc.research.domain.transaction.ResearchDto;
import com.cnsc.research.service.ResearchService;
import com.opencsv.exceptions.CsvException;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/research")
public class ResearchController {

    private final ResearchService service;
    private final Logger logger;

    @Autowired
    public ResearchController(ResearchService service, Logger logger){
        this.service = service;
        this.logger = logger;
    }

    @PostMapping("/upload")
    public List<ResearchDto> uploadCsv(@RequestParam("file") MultipartFile file) throws InvalidCsvFieldException, IOException, CsvException {
        return service.getResearchesFromCsv(file);
    }
}
