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
import org.springframework.http.ResponseEntity;
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

    @PostMapping("/pdf")
    public String uploadPdf(@RequestParam(name = "id", defaultValue = "0") Integer id, @RequestParam("title") String title, @RequestParam("file") MultipartFile file) throws FileNotFoundException, InvalidFileFormat, FileAlreadyExistsException {
        return service.processPdf(id, title, file);
    }

    @DeleteMapping("/pdf")
    public String deletePdf(@RequestParam("title") String title) {
        return service.deletePdf(title);
    }

    @PostMapping
    public ResearchBatchSaveResponse saveResearch(@RequestBody ResearchDto researchDto) {
        return service.saveResearch(researchDto);
    }

    @PutMapping
    public ResponseEntity updateResearch(@RequestBody ResearchDto researchDto) {
        return service.updateResearch(researchDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteResearch(@PathVariable(name = "id") Integer researchId) {
        return service.deleteResearch(researchId);
    }

    @PostMapping("/batch")
    public List<ResearchBatchSaveResponse> saveResearches(@RequestBody List<ResearchDto> researchDtos) {
        return service.saveResearches(researchDtos);
    }

    @GetMapping("/list")
    public ResearchBatchQueryResponse getAllResearches(@RequestParam int page,
                                                       @RequestParam(defaultValue = "max") String size,
                                                       @RequestParam(defaultValue = "title") String sortBy) {
        return service.getAllResearches(page, size.equals("max") ? Integer.MAX_VALUE : Integer.valueOf(size), sortBy);
    }

}
