package com.cnsc.research.service;

import com.cnsc.research.domain.model.analysis.*;
import com.cnsc.research.domain.repository.*;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.OK;

@Service
public class AnalysisService {

    private final UserRepository userRepository;
    private final ResearchRepository researchRepository;
    private final PresentationRepository presentationRepository;
    private final PublicationRepository publicationRepository;
    private final ResearchersRepository researchersRepository;
    private final ResearchAgendaRepository researchAgendaRepository;
    private final DeliveryUnitRepository deliveryUnitRepository;
    private final FundingAgencyRepository fundingAgencyRepository;

    private final Logger logger;

    @Autowired
    public AnalysisService(UserRepository userRepository, ResearchRepository researchRepository, PresentationRepository presentationRepository, PublicationRepository publicationRepository, ResearchersRepository researchersRepository, ResearchAgendaRepository researchAgendaRepository, DeliveryUnitRepository deliveryUnitRepository, FundingAgencyRepository fundingAgencyRepository, Logger logger) {
        this.userRepository = userRepository;
        this.researchRepository = researchRepository;
        this.presentationRepository = presentationRepository;
        this.publicationRepository = publicationRepository;
        this.researchersRepository = researchersRepository;
        this.researchAgendaRepository = researchAgendaRepository;
        this.deliveryUnitRepository = deliveryUnitRepository;
        this.fundingAgencyRepository = fundingAgencyRepository;
        this.logger = logger;
    }

    public ResponseEntity getAgendaResearchCount() {
        try {
            List<AgendaCount> result = researchAgendaRepository.retrieveAgendaCount();
            return new ResponseEntity<List<AgendaCount>>(result, OK);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            return new ResponseEntity<String>("Error on retrieving agenda count", INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity getDeliveryUnitCount() {
        try {
            List<DeliveryUnitCount> result = deliveryUnitRepository.findAll()
                    .stream()
                    .map(item -> new DeliveryUnitCount(item.getUnitName(), item.getResearchByUnitId().size()))
                    .collect(Collectors.toList());
            return new ResponseEntity<List<DeliveryUnitCount>>(result, OK);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            return new ResponseEntity<String>("Error on retrieving agenda count", INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity getFundingAgencyCount() {
        try {
            List<FundingAgencyCount> result = fundingAgencyRepository.findAll()
                    .stream()
                    .map(item -> new FundingAgencyCount(item.getAgencyName(), item.getResearchSet().size()))
                    .collect(Collectors.toList());
            return new ResponseEntity<List<FundingAgencyCount>>(result, OK);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            return new ResponseEntity<String>("Error on retrieving agenda count", INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity getResearcherCount() {
        try {
            List<ResearcherCount> result = researchersRepository.findByDeletedFalseOrderByNameAsc()
                    .stream()
                    .map(item -> new ResearcherCount(item.getName(), item.getResearches().size()))
                    .collect(Collectors.toList());
            return new ResponseEntity<List<ResearcherCount>>(result, OK);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            return new ResponseEntity<String>("Error on retrieving agenda count", INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity getResearchStatusCount() {
        try {
            List<StatusCount> result = researchRepository.getStatusCount();
            return new ResponseEntity<List<StatusCount>>(result, OK);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            return new ResponseEntity<String>("Error on retrieving agenda count", INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity getResearchRelatedCounts() {
        try {
            Map<String, Object> counts = new HashMap<>();
            counts.put("agenda", getAgendaResearchCount().getBody());
            counts.put("deliveryUnit", getDeliveryUnitCount().getBody());
            counts.put("fundingAgency", getFundingAgencyCount().getBody());
            counts.put("researcher", getResearcherCount().getBody());
            counts.put("status", getResearchStatusCount().getBody());
            return new ResponseEntity<Map<String, Object>>(counts, OK);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            return new ResponseEntity("Error on retrieving research statistics", INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity getInitialStatistics() {
        try {
            Map<String, Object> data = new HashMap<>();
            data.put("research", researchRepository.countByDeletedIsFalse());
            data.put("presentation", presentationRepository.countByDeletedFalse());
            data.put("publication", publicationRepository.countByDeletedIsFalse());
            data.put("user", userRepository.countByDeletedIsFalse());
            return new ResponseEntity<Map<String, Object>>(data, OK);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            return new ResponseEntity("Error on retrieving initial statistics", INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity getPresentationStatistics() {
        try {
            return new ResponseEntity(presentationRepository.getTypeCounts(), OK);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            return new ResponseEntity("Error on retrieving presentation statistics", INTERNAL_SERVER_ERROR);
        }
    }

}
