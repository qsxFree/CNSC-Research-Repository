package com.cnsc.research.api;

import com.cnsc.research.domain.transaction.PresentationDto;
import com.cnsc.research.domain.transaction.PresentationQueryBuilder;
import com.cnsc.research.service.PresentationService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/presentation")
public class PresentationController {

    private final PresentationService service;
    private final Logger logger;

    @Autowired
    public PresentationController(PresentationService service, Logger logger) {
        this.service = service;
        this.logger = logger;
    }

    @GetMapping("/{id}")
    public PresentationDto getPresentation(@PathVariable(name = "id") Long presentationId) {
        return service.getPresentation(presentationId);
    }

    @PostMapping
    public ResponseEntity addPresentation(@RequestBody PresentationDto presentationDto) {
        return service.addPresentation(presentationDto);
    }

    @PutMapping
    public ResponseEntity editPresentation(@RequestBody PresentationDto presentationDto) {
        return service.editPresentation(presentationDto);
    }

    @DeleteMapping("/{id}")
    public String deletePresentation(@PathVariable(name = "id") Long presentationId) {
        return service.deletePresentation(presentationId);
    }

    @PostMapping("/list")
    public ResponseEntity saveBatch(@RequestBody List<PresentationDto> presentationDtos) {
        try {
            return service.savePresentations(presentationDtos);
        } catch (Exception e) {
            return new ResponseEntity("Error on saving a batch of presentation", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/list")
    public List<PresentationDto> getPresentations() {
        return service.getPresentations();
    }

    @DeleteMapping("/list")
    public ResponseEntity deletePresentation(@RequestBody List<Long> idList) {
        return service.deletePresentations(idList);
    }

    @GetMapping("/list/search")
    public List<PresentationDto> getPresentationByTitle(@RequestParam String title) {
        return service.getPresentationByTitle(title);
    }

    @PostMapping("/list/search")
    public List<PresentationDto> getPresentationAdvanced(@RequestBody PresentationQueryBuilder queryBuilder) {
        return service.getPresentationByAdvancedFilter(queryBuilder);
    }

    @PostMapping("/import")
    public List<PresentationDto> uploadCsv(@RequestParam("file") MultipartFile incomingFile) {
        try {
            return service.getPresentationFromFile(incomingFile);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @PutMapping("/visibility/{id}")
    public ResponseEntity triggerVisibility(@PathVariable(name = "id") Long id) {
        return service.triggerVisibility(id);
    }
}
