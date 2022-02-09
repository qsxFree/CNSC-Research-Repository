package com.cnsc.research.api;

import com.cnsc.research.domain.model.EntityType;
import com.cnsc.research.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/log")
public class LogController {

    private final LogService logService;

    @Autowired
    public LogController(LogService logService) {
        this.logService = logService;
    }

    @GetMapping("presentation")
    public ResponseEntity getPresentationLogs() {
        return logService.getLogs(EntityType.PRESENTATION);
    }

    @GetMapping("presentation/{dataId}")
    public ResponseEntity getPresentationLog(@PathVariable(name = "dataId") Long dataId) {
        return logService.getLog(EntityType.PRESENTATION, dataId);
    }

    @GetMapping("research")
    public ResponseEntity getResearchLogs() {
        return logService.getLogs(EntityType.RESEARCH);
    }

    @GetMapping("research/{dataId}")
    public ResponseEntity getResearchLog(@PathVariable(name = "dataId") Long dataId) {
        return logService.getLog(EntityType.RESEARCH, dataId);
    }

    @GetMapping("publication")
    public ResponseEntity getPublicationLogs() {
        return logService.getLogs(EntityType.PUBLICATION);
    }

    @GetMapping("publication/{dataId}")
    public ResponseEntity getPublicationLog(@PathVariable(name = "dataId") Long dataId) {
        return logService.getLog(EntityType.PUBLICATION, dataId);
    }

}
