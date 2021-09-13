package com.cnsc.research.service;

import com.cnsc.research.domain.mapper.PublicationMapper;
import com.cnsc.research.domain.mapper.ResearcherMapper;
import com.cnsc.research.domain.model.Publication;
import com.cnsc.research.domain.repository.PublicationRepository;
import com.cnsc.research.domain.transaction.ExtendedPublicationDto;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PublicationService {
    private final PublicationRepository repository;
    private final Logger logger;
    private final PublicationMapper publicationMapper;
    private final ResearcherMapper researcherMapper;

    @Autowired
    public PublicationService(PublicationRepository repository,
                              PublicationMapper publicationMapper,
                              Logger logger,
                              ResearcherMapper researcherMapper) {
        this.repository = repository;
        this.logger = logger;
        this.publicationMapper = publicationMapper;
        this.researcherMapper = researcherMapper;
    }

    public String addPublication(ExtendedPublicationDto publicationDto){
        Publication publication = publicationMapper.toPublication(publicationDto);
        publication.setResearchers(researcherMapper.toResearcher(publicationDto.getResearchers()));
        try{
            repository.save(publication);
            return "New Record has been added";
        }catch (Exception e){
            e.printStackTrace();
            return e.getMessage();
        }
    }
}
