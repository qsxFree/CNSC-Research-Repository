package com.cnsc.research.api;

import com.cnsc.research.service.AnalysisService;
import com.cnsc.research.service.ResearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/analysis")
public class AnalysisController {

    private final AnalysisService service;
    private final ResearchService researchService;

    @Autowired
    public AnalysisController(AnalysisService service, ResearchService researchService) {
        this.service = service;
        this.researchService = researchService;
    }

    @GetMapping("/research/counts")
    public ResponseEntity getResearchRelatedCounts() {
        return service.getResearchRelatedCounts();
    }

    @GetMapping("/research/budget")
    public ResponseEntity getBudgetByYear() {
        return service.getBudgetByYear();
    }

    @GetMapping("/research/top")
    public ResponseEntity getResearchTopData() {
        return researchService.getTopData();
    }

    @GetMapping("/agenda")
    public ResponseEntity getAgendaResearchCount() {
        return service.getResearchAgendaCount();
    }

    @GetMapping("/delivery-unit")
    public ResponseEntity getDeliveryUnitCount() {
        return service.getDeliveryUnitCount();
    }

    @GetMapping("/funding-agency")
    public ResponseEntity getFundingAgencyCount() {
        return service.getFundingAgencyCount();
    }

    @GetMapping("/researcher")
    public ResponseEntity getResearcherCount() {
        return service.getResearcherCount();
    }

    @GetMapping("/status")
    public ResponseEntity getResearchStatusCount() {
        return service.getResearchStatusCount();
    }

    @GetMapping("/initial")
    public ResponseEntity getInitialStatistics() {
        return service.getInitialStatistics();
    }

    @GetMapping("/presentation/type")
    public ResponseEntity getPresentationStatistic() {
        return service.getPresentationStatistics();
    }
}
