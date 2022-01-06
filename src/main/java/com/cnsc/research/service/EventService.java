package com.cnsc.research.service;

import com.cnsc.research.domain.mapper.EventMapper;
import com.cnsc.research.domain.mapper.PresentationMapper;
import com.cnsc.research.domain.mapper.ResearchMapper;
import com.cnsc.research.domain.model.Presentation;
import com.cnsc.research.domain.model.Research;
import com.cnsc.research.domain.repository.EventRepository;
import com.cnsc.research.domain.repository.PresentationRepository;
import com.cnsc.research.domain.repository.ResearchRepository;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventService {

    private final EventRepository repository;
    private final Logger logger;
    private final EventMapper mapper;
    private final PresentationRepository presentationRepository;
    private final ResearchRepository researchRepository;
    private final ResearchMapper researchMapper;
    private final PresentationMapper presentationMapper;

    @Autowired
    public EventService(EventRepository repository, Logger logger, EventMapper mapper, PresentationRepository presentationRepository, ResearchRepository researchRepository, ResearchMapper researchMapper, PresentationMapper presentationMapper) {
        this.repository = repository;
        this.logger = logger;
        this.mapper = mapper;
        this.presentationRepository = presentationRepository;
        this.researchRepository = researchRepository;
        this.researchMapper = researchMapper;
        this.presentationMapper = presentationMapper;
    }

    public ResponseEntity getPresentationDateByMonths(String yearMonth) {
        try {
            String[] values = yearMonth.split("-");
            int year = Integer.parseInt(values[0]);
            int month = Integer.parseInt(values[1]);
            List<Presentation> presentationList = presentationRepository.getPresentationsByEventDate(year, month);
            return new ResponseEntity(presentationMapper.toTransaction(presentationList), HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            return new ResponseEntity<String>("Error on retrieving presentation date", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity getResearchEndDate(String yearMonth) {
        try {
            String[] values = yearMonth.split("-");
            int year = Integer.parseInt(values[0]);
            int month = Integer.parseInt(values[1]);
            List<Research> researchList = researchRepository.getResearchByEndDate(year, month);
            return new ResponseEntity(researchMapper.toTransaction(researchList), HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            return new ResponseEntity<String>("Error on retrieving research end date", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
