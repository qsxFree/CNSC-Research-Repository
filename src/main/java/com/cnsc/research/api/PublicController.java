package com.cnsc.research.api;

import com.cnsc.research.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public")
public class PublicController {

    private final ResearchService researchService;
    private final PublicationService publicationService;
    private final PresentationService presentationService;
    private final DeliveryUnitService deliveryUnitService;
    private final FundingAgencyService fundingAgencyService;
    private final ResearcherService researcherService;
    private final ResearchAgendaService researchAgendaService;

    @Autowired
    public PublicController(ResearchService researchService, PublicationService publicationService, PresentationService presentationService, DeliveryUnitService deliveryUnitService, FundingAgencyService fundingAgencyService, ResearcherService researcherService, ResearchAgendaService researchAgendaService) {
        this.researchService = researchService;
        this.publicationService = publicationService;
        this.presentationService = presentationService;
        this.deliveryUnitService = deliveryUnitService;
        this.fundingAgencyService = fundingAgencyService;
        this.researcherService = researcherService;
        this.researchAgendaService = researchAgendaService;
    }

    @GetMapping("/research")
    public ResponseEntity retrieveAllResearch() {
        return researchService.getPublicResearch();
    }

    @GetMapping("/research/search")
    public ResponseEntity retrieveByResearchTitle(@RequestParam(required = true) String title) {
        try {
            return new ResponseEntity(researchService.getResearchByTitle(title), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity("Error on retrieving Researches", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/research/{id}")
    public ResponseEntity retrieveResearch(@PathVariable(name = "id") Integer researchId) {
        return researchService.getResearch(researchId);
    }

    @GetMapping("/publication")
    public ResponseEntity retrieveAllPublication() {
        return publicationService.getPublicPublication();
    }

    @GetMapping("/presentation")
    public ResponseEntity retrieveAllPresentation() {
        return presentationService.getPublicPresentations();
    }

    @GetMapping("/presentation/search")
    public ResponseEntity retrieveByPresentationTitle(@RequestParam(required = true) String title) {
        try {
            return new ResponseEntity(presentationService.getPresentationByTitle(title), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity("Error on retrieving presentation", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/presentation/{id}")
    public ResponseEntity retrieveAllPresentation(@PathVariable(name = "id") Long presentationId) {
        return presentationService.getPresentation(presentationId);
    }

    @GetMapping("/delivery-unit")
    public ResponseEntity retrieveAllDeliveryUnit() {
        return deliveryUnitService.getDistinctDeliveryUnitName();
    }

    @GetMapping("/delivery-unit/research/{id}")
    public ResponseEntity retrieveResearchByDeliveryUnit(@PathVariable(name = "id") Integer id) {
        return deliveryUnitService.getResearches(id);
    }

    @GetMapping("/funding-agency")
    public ResponseEntity retrieveAllFundingAgency() {
        return fundingAgencyService.getDistinctFundingAgency();
    }

    @GetMapping("/funding-agency/research/{id}")
    public ResponseEntity retrieveResearchByFundingAgency(@PathVariable(name = "id") Integer id) {
        return fundingAgencyService.getResearches(id);
    }

    @GetMapping("/researchers")
    public ResponseEntity retrieveAllResearchers() {
        return researcherService.getDistinctResearchers();
    }

    @GetMapping("/researchers/research/{id}")
    public ResponseEntity retrieveResearchByResearchers(@PathVariable(name = "id") Integer id) {
        return researcherService.getResearches(id);
    }

    @GetMapping("/research-agenda")
    public ResponseEntity retrieveAllResearchAgenda() {
        return researchAgendaService.retrieveAllResearch();
    }

    @GetMapping("/research-agenda/research/{agenda}")
    public ResponseEntity retrieveResearchByAgenda(@PathVariable(name = "agenda") String agenda) {
        return researchAgendaService.getResearches(agenda);
    }


    @GetMapping("/status/research/{status}")
    public ResponseEntity retrieveResearchByStatus(@PathVariable(name = "status") String status) {
        return researchService.getResearchByStatus(status);
    }

    @GetMapping("/type/presentation/{type}")
    public ResponseEntity retrievePresentationByType(@PathVariable(name = "type") String type) {
        return presentationService.getPresentationByType(type);
    }

    @GetMapping("/researchers/presentation/{id}")
    public ResponseEntity retrievePresentationByResearcher(@PathVariable(name = "id") Integer id) {
        return presentationService.getPresentationByResearcher(id);
    }
}
