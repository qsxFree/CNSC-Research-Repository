package com.cnsc.research.api;

import com.cnsc.research.service.AnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/public")
public class PublicAnalysisController {
    private final AnalysisService service;

    @Autowired
    public PublicAnalysisController(AnalysisService service) {
        this.service = service;
    }

    @GetMapping("/research/counts")
    public ResponseEntity getResearchRelatedCounts() {
        return service.getResearchRelatedCounts();
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
