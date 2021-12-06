package com.cnsc.research.api;

import com.cnsc.research.domain.transaction.ResearchBatchQueryResponse;
import com.cnsc.research.domain.transaction.ResearchBatchSaveResponse;
import com.cnsc.research.domain.transaction.ResearchDto;
import com.cnsc.research.domain.transaction.ResearchQueryBuilder;
import com.cnsc.research.service.ResearchService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @PostMapping("/import")
    public List<ResearchDto> uploadCsv(@RequestParam("file") MultipartFile file) throws Exception {
        try {
            return service.getResearchesFromCsv(file);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @PostMapping("/pdf")
    public String uploadPdf(@RequestParam("file") MultipartFile file) {
        return service.processPdf(file);
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

    @GetMapping("/{id}")
    public ResponseEntity getResearch(@PathVariable(name = "id") Integer researchId) {
        return service.getResearch(researchId);
    }

    @PostMapping("/batch")
    public List<ResearchBatchSaveResponse> saveResearches(@RequestBody List<ResearchDto> researchDtos) {
        return service.saveResearches(researchDtos);
    }

    @DeleteMapping("/batch")
    public ResponseEntity deleteBatch(@RequestBody List<Integer> ids) {
        return service.deleteResearches(ids);
    }

    @GetMapping("/batch")
    public ResearchBatchQueryResponse getAllResearches(@RequestParam int page,
                                                       @RequestParam(defaultValue = "max") String size,
                                                       @RequestParam(defaultValue = "title") String sortBy) {
        return service.getAllResearches(page, size.equals("max") ? Integer.MAX_VALUE : Integer.valueOf(size), sortBy);
    }

    @GetMapping("/list")
    public List<ResearchDto> getResearches() {
        return service.getResearches();
    }

    @GetMapping("/list/search")
    public List<ResearchDto> getResearchByTitle(@RequestParam(required = true) String title) {
        return service.getResearchByTitle(title);
    }

    @PostMapping("/list/search")
    public List<ResearchDto> getResearchAdvanced(@RequestBody ResearchQueryBuilder queryBuilder) {
        return service.getResearchByAdvancedFilter(queryBuilder);
    }

    @GetMapping("/budget/max")
    public Double getMaxBudget() {
        return service.getMaxBudget();
    }


}
