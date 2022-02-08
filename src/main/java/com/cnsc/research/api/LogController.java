package com.cnsc.research.api;

import com.cnsc.research.service.PresentationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/log")
public class LogController {

    private final PresentationService presentationService;

    @Autowired
    public LogController(PresentationService presentationService) {
        this.presentationService = presentationService;
    }

    @GetMapping("presentation")
    public ResponseEntity getPresentationLogs() {
        return presentationService.getLogs();
    }

    @GetMapping("presentation/{dataId}")
    public ResponseEntity getPresentationLog(@PathVariable(name = "dataId") Long dataId) {
        return presentationService.getLog(dataId);
    }
}
