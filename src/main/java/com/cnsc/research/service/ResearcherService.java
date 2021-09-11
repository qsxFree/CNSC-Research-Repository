package com.cnsc.research.service;

import com.cnsc.research.domain.mapper.ResearcherMapper;
import com.cnsc.research.domain.repository.ResearchersRepository;
import com.cnsc.research.domain.transaction.ResearchersDto;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResearcherService {
    private final ResearchersRepository repository;
    private final Logger logger;

    @Autowired
    public ResearcherService(ResearchersRepository repository, Logger logger) {
        this.repository = repository;
        this.logger = logger;
    }

    public List<ResearchersDto> getAllResearchers() {
        return ResearcherMapper.toResearchDto(repository.findAll());
    }
}
