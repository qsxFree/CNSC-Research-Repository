package com.cnsc.research.api;

import com.cnsc.research.service.ResearchAgendaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/research-agenda")
public class ResearchAgendaController {

    private final ResearchAgendaService service;

    @Autowired
    public ResearchAgendaController(ResearchAgendaService service) {
        this.service = service;
    }

    @GetMapping("/list")
    public ResponseEntity getResearchAgendaList() {
        return service.retrieveAllResearch();
    }
}
