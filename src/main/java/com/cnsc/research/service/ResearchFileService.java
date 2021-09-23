package com.cnsc.research.service;

import com.cnsc.research.domain.model.ResearchFile;
import com.cnsc.research.domain.repository.ResearchFileRepository;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ResearchFileService {
    private final ResearchFileRepository repository;
    private final Logger logger;

    @Autowired
    public ResearchFileService(ResearchFileRepository repository, Logger logger) {
        this.repository = repository;
        this.logger = logger;
    }

    public List<String> getTitles() {
        return repository.getTitles()
                .stream()
                .map(ResearchFile::getTitle)
                .collect(Collectors.toList());
    }
}
