package com.cnsc.research.api;

import com.cnsc.research.service.PresentationService;
import com.cnsc.research.service.PublicationService;
import com.cnsc.research.service.ResearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/public")
public class PublicController {

    private final ResearchService researchService;
    private final PublicationService publicationService;
    private final PresentationService presentationService;

    @Autowired
    public PublicController(ResearchService researchService, PublicationService publicationService, PresentationService presentationService) {
        this.researchService = researchService;
        this.publicationService = publicationService;
        this.presentationService = presentationService;
    }

    @GetMapping("/research")
    public ResponseEntity retrieveAllResearch() {
        return researchService.getPublicResearch();
    }

    @GetMapping("/research/{id}")
    public ResponseEntity retrieveResearch(@PathVariable(name = "id") Integer researchId) {
        return researchService.getResearch(researchId);
    }

    @GetMapping("/publication")
    public ResponseEntity retrieveAllPublication() {
        return publicationService.getPublicPublication();
    }

    @GetMapping("/presentation")
    public ResponseEntity retrieveAllPresentation() {
        return presentationService.getPublicPresentations();
    }

    @GetMapping("/presentation/{id}")
    public ResponseEntity retrieveAllPresentation(@PathVariable(name = "id") Long presentationId) {
        return presentationService.getPresentation(presentationId);
    }
}
