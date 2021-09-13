package com.cnsc.research.api;

import com.cnsc.research.domain.transaction.ExtendedPublicationDto;
import com.cnsc.research.service.PublicationService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/publication")
public class PublicationController {
    private final PublicationService service;
    private final Logger logger;

    @Autowired
    public PublicationController(PublicationService service, Logger logger){
        this.logger = logger;
        this.service = service;
    }

    @PostMapping
    public String addPublication(@RequestBody ExtendedPublicationDto publication){
        return service.addPublication(publication);
    }

}
