package com.cnsc.research.api;

import com.cnsc.research.domain.exception.InvalidCsvFieldException;
import com.cnsc.research.domain.exception.InvalidFileFormat;
import com.cnsc.research.domain.transaction.ResearchBatchQueryResponse;
import com.cnsc.research.domain.transaction.ResearchBatchSaveResponse;
import com.cnsc.research.domain.transaction.ResearchDto;
import com.cnsc.research.service.ResearchService;
import com.opencsv.exceptions.CsvException;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.util.List;

@RestController
@RequestMapping("/api/research")
public class ResearchController {

    private final ResearchService service;
    private final Logger logger;

    @Autowired
    public ResearchController(ResearchService service, Logger logger) {
        this.service = service;
        this.logger = logger;
    }

    @PostMapping("/upload-csv")
    public List<ResearchDto> uploadCsv(@RequestParam("file") MultipartFile file) throws InvalidCsvFieldException, IOException, CsvException {
        return service.getResearchesFromCsv(file);
    }

    @PostMapping("/upload-pdf")
    public String uploadPdf(@RequestParam("title") String title, @RequestParam("file") MultipartFile file) throws FileAlreadyExistsException, FileNotFoundException, InvalidFileFormat {
        return service.processPdf(title, file);
    }

    @PostMapping
    public ResearchBatchSaveResponse saveResearch(@RequestBody ResearchDto researchDto) {
        return service.saveResearch(researchDto);
    }

    @PostMapping("/batch")
    public List<ResearchBatchSaveResponse> saveResearches(@RequestBody List<ResearchDto> researchDtos) {
        return service.saveResearches(researchDtos);
    }

    @GetMapping("/list")
    public ResearchBatchQueryResponse getAllResearches(@RequestParam int page,
                                                       @RequestParam(defaultValue = "20") int size,
                                                       @RequestParam(defaultValue = "title") String sortBy) {
        return service.getAllResearches(page, size, sortBy);
    }

}
