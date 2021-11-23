package com.cnsc.research.api;

import com.cnsc.research.service.EventService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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

    
}
