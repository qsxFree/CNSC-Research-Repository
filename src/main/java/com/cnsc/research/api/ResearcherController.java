package com.cnsc.research.api;

import com.cnsc.research.domain.transaction.ResearchersDto;
import com.cnsc.research.service.ResearcherService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/researcher")
public class ResearcherController {
    private final Logger logger;
    private final ResearcherService service;
    @Autowired
    public ResearcherController(ResearcherService service,Logger logger) {
        this.logger = logger;
        this.service = service;
    }

    @GetMapping("/list")
    public List<ResearchersDto> getAllResearchers(){
        return service.getAllResearchers();
    }

}
