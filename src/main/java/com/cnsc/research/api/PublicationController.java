package com.cnsc.research.api;

import com.cnsc.research.domain.transaction.ExtendedPublicationDto;
import com.cnsc.research.domain.transaction.PublicationDto;
import com.cnsc.research.domain.transaction.PublicationSaveResponse;
import com.cnsc.research.service.PublicationService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/publication")
public class PublicationController {
    private final PublicationService service;
    private final Logger logger;

    @Autowired
    public PublicationController(PublicationService service, Logger logger) {
        this.logger = logger;
        this.service = service;
    }

    @PostMapping
    public PublicationSaveResponse addPublication(@RequestBody ExtendedPublicationDto publication) {
        return service.addPublication(publication);
    }

    @GetMapping(path = "/{id}")
    public ExtendedPublicationDto getPublication(@PathVariable(name = "id") Long publicationId) {
        return service.getPublication(publicationId);
    }

    @PutMapping
    public String editPublication(@RequestBody PublicationDto publicationDto) {
        return service.editPublication(publicationDto);
    }

    @DeleteMapping
    public String deletePublication(@RequestBody Long id) {
        return service.deletePublication(id);
    }

    @GetMapping("/list")
    public List<ExtendedPublicationDto> getPublications() {
        return service.getPublications();
    }

    @PostMapping("/list")
    public List<PublicationSaveResponse> saveBatch(@RequestBody List<ExtendedPublicationDto> publicationDtos) {
        return service.savePublications(publicationDtos);
    }

    @DeleteMapping("/list")
    public ResponseEntity deletePublications(@RequestBody List<Long> idList){
        return service.deletePublications(idList);
    }

    @PostMapping("/xls")
    public List<ExtendedPublicationDto> uploadCsv(@RequestParam("file") MultipartFile incomingFile) {
        try {
            return service.getPublicationFromFile(incomingFile);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
