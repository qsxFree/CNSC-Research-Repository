package com.cnsc.research.api;

import com.cnsc.research.service.EventService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/event")
public class EventController {

    private final EventService service;
    private final Logger logger;

    @Autowired
    public EventController(EventService service, Logger logger) {
        this.service = service;
        this.logger = logger;
    }

    @GetMapping("/presentation/{yearMonth}")
    public ResponseEntity getPresentationByDate(@PathVariable(name = "yearMonth") String yearMonth) {
        return this.service.getPresentationDateByMonths(yearMonth);
    }

    @GetMapping("/research/{yearMonth}")
    public ResponseEntity getResearchByDate(@PathVariable(name = "yearMonth") String yearMonth) {
        return this.service.getResearchEndDate(yearMonth);
    }

}
