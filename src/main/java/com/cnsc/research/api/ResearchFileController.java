package com.cnsc.research.api;

import com.cnsc.research.service.ResearchFileService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/research-file")
public class ResearchFileController {

    private final ResearchFileService service;
    private final Logger logger;

    @Autowired
    public ResearchFileController(ResearchFileService service, Logger logger) {
        this.service = service;
        this.logger = logger;
    }

    @GetMapping("/titles")
    private List<String> getTitles() {
        return service.getTitles();
    }
}
