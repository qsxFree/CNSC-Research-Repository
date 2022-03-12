package com.cnsc.research.api;

import com.cnsc.research.service.PresentationService;
import com.cnsc.research.service.PublicationService;
import com.cnsc.research.service.ResearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/archive")
public class ArchiveController {
    private final ResearchService researchService;
    private final PresentationService presentationService;
    private final PublicationService publicationService;

    @Autowired
    public ArchiveController(ResearchService researchService, PresentationService presentationService, PublicationService publicationService) {
        this.researchService = researchService;
        this.presentationService = presentationService;
        this.publicationService = publicationService;
    }

    @GetMapping("research")
    public ResponseEntity getResearches() {
        return this.researchService.getArchivedData();
    }

    @GetMapping("presentation")
    public ResponseEntity getPresentations() {
        return this.presentationService.getArchivedData();
    }

    @GetMapping("publication")
    public ResponseEntity getPublications() {
        return this.publicationService.getArchivedData();
    }

    @PostMapping("research/{id}")
    public ResponseEntity restoreResearch(@PathVariable(name = "id") Long id) {
        return researchService.restoreData(id);
    }

    @PostMapping("presentation/{id}")
    public ResponseEntity restorePresentation(@PathVariable(name = "id") Long id) {
        return presentationService.restoreData(id);
    }

    @PostMapping("publication/{id}")
    public ResponseEntity restorePublication(@PathVariable(name = "id") Long id) {
        return publicationService.restoreData(id);
    }

}
