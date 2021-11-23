package com.cnsc.research.service;

import com.cnsc.research.domain.mapper.EventMapper;
import com.cnsc.research.domain.repository.EventRepository;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EventService {

    private final EventRepository repository;
    private final Logger logger;
    private final EventMapper mapper;

    @Autowired
    public EventService(EventRepository repository, Logger logger, EventMapper mapper) {
        this.repository = repository;
        this.logger = logger;
        this.mapper = mapper;
    }


}
