package com.cnsc.research.api;

import com.cnsc.research.service.ResearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/public")
public class PublicController {

    private final ResearchService researchService;

    @Autowired
    public PublicController(ResearchService researchService) {
        this.researchService = researchService;
    }

    @GetMapping("/research")
    public ResponseEntity retrieveAllResearch() {
        return researchService.getPublicResearch();
    }
}
