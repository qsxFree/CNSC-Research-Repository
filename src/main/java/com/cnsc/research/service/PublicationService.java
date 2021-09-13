package com.cnsc.research.service;

import com.cnsc.research.domain.mapper.PublicationMapper;
import com.cnsc.research.domain.mapper.ResearcherMapper;
import com.cnsc.research.domain.model.Publication;
import com.cnsc.research.domain.model.Researchers;
import com.cnsc.research.domain.repository.PublicationRepository;
import com.cnsc.research.domain.transaction.ExtendedPublicationDto;
import com.cnsc.research.misc.EntityBuilders;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PublicationService {
    private final PublicationRepository repository;
    private final Logger logger;
    private final PublicationMapper publicationMapper;
    private final EntityBuilders entityBuilders;

    @Autowired
    public PublicationService(PublicationRepository repository,
                              PublicationMapper publicationMapper,
                              Logger logger,
                              EntityBuilders entityBuilders
                              ) {
        this.repository = repository;
        this.logger = logger;
        this.publicationMapper = publicationMapper;
        this.entityBuilders = entityBuilders;
    }

    public String addPublication(ExtendedPublicationDto publicationDto){
        Publication publication = publicationMapper.toPublication(publicationDto);

        //REMINDER -- this will cause a performance issue someday.
        // run profiling and change the implementation.
        List<Researchers> researchers = publication.getResearchers()
                .stream()
                .map(item -> entityBuilders.buildResearcher(item.getName()))
                .collect(Collectors.toList());

        publication.setResearchers(researchers);

        try{
            repository.save(publication);
            return "New Record has been added";
        }catch (Exception e){
            e.printStackTrace();
            return e.getMessage();
        }
    }
}
