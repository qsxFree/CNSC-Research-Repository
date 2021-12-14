package com.cnsc.research.service;

import com.cnsc.research.domain.mapper.ResearchAgendaMapper;
import com.cnsc.research.domain.mapper.ResearchMapper;
import com.cnsc.research.domain.model.Research;
import com.cnsc.research.domain.repository.ResearchAgendaRepository;
import com.cnsc.research.domain.repository.ResearchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.OK;

@Service
public class ResearchAgendaService {

    private final ResearchAgendaRepository repository;
    private final ResearchAgendaMapper mapper;
    private final ResearchRepository researchRepository;
    private final ResearchMapper researchMapper;

    @Autowired
    public ResearchAgendaService(ResearchAgendaRepository repository, ResearchAgendaMapper mapper, ResearchRepository researchRepository, ResearchMapper researchMapper) {
        this.repository = repository;
        this.mapper = mapper;
        this.researchRepository = researchRepository;
        this.researchMapper = researchMapper;
    }

    public ResponseEntity retrieveAllResearch() {
        try {
            return new ResponseEntity<List<String>>(repository.findDistinct(), OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<String>("Error on retrieving research agenda list", INTERNAL_SERVER_ERROR);
        }
    }


    public ResponseEntity getResearches(String agenda) {
        try {
            List<Research> researchList = researchRepository.findByResearchAgendaList_AgendaIgnoreCaseOrderByResearchFile_TitleAsc(agenda);
            return new ResponseEntity(researchMapper.toTransaction(researchList), OK);
        } catch (Exception e) {
            return new ResponseEntity("Error on retrieving researches", INTERNAL_SERVER_ERROR);
        }
    }
}
