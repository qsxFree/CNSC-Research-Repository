package com.cnsc.research.service;

import com.cnsc.research.domain.mapper.ResearchAgendaMapper;
import com.cnsc.research.domain.repository.ResearchAgendaRepository;
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

    @Autowired
    public ResearchAgendaService(ResearchAgendaRepository repository, ResearchAgendaMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public ResponseEntity retrieveAllResearch() {
        try {
            return new ResponseEntity<List<String>>(repository.findDistinct(), OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<String>("Error on retrieving research agenda list", INTERNAL_SERVER_ERROR);
        }
    }
}
