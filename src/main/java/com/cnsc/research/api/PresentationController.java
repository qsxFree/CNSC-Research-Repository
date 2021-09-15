package com.cnsc.research.api;

import com.cnsc.research.domain.transaction.PresentationDto;
import com.cnsc.research.domain.transaction.PresentationSaveResponse;
import com.cnsc.research.service.PresentationService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/presentation")
public class PresentationController {

    private final PresentationService service;
    private final Logger logger;

    @Autowired
    public PresentationController(PresentationService service, Logger logger) {
        this.service = service;
        this.logger = logger;
    }

    @GetMapping("/{id}")
    public PresentationDto getPresentation(@PathVariable(name = "id") Long presentationId){
        return service.getPresentation(presentationId);
    }

    @PostMapping
    public PresentationSaveResponse addPresentation(@RequestBody PresentationDto presentationDto){
        try {
            return service.addPresentation(presentationDto);
        } catch (Exception e) {
            e.printStackTrace();
            return new PresentationSaveResponse("","An err has occured. Error - " + e.getMessage());
        }
    }
}
