package com.cnsc.research.service;

import com.cnsc.research.domain.mapper.PresentationMapper;
import com.cnsc.research.domain.model.Presentation;
import com.cnsc.research.domain.model.PresentationType;
import com.cnsc.research.domain.repository.PresentationRepository;
import com.cnsc.research.domain.transaction.PresentationDto;
import com.cnsc.research.domain.transaction.PresentationSaveResponse;
import com.cnsc.research.misc.EntityBuilders;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PresentationService {

    private final PresentationRepository repository;
    private final PresentationMapper mapper;
    private final Logger logger;
    private final EntityBuilders builders;

    @Autowired
    public PresentationService(PresentationRepository repository, PresentationMapper mapper, Logger logger, EntityBuilders builders) {
        this.repository = repository;
        this.mapper = mapper;
        this.logger = logger;
        this.builders = builders;
    }

    public PresentationDto getPresentation(Long presentationId) {
        Optional<Presentation> result = repository.findByPresentationIdAndDeletedIsFalse(presentationId);
        return result.isPresent() ? mapper.toPresentationDto(result.get()) : null;
    }

    public PresentationSaveResponse addPresentation(PresentationDto presentationDto) throws Exception {
        //the toPresentation mappings will do a database operation.
        //It will search for the existence of the research based on title
        Presentation presentation = mapper.toPresentation(presentationDto);
        String researchTitle = presentation.getResearch().getResearchFile().getTitle();
        PresentationType type = presentation.getType();
        if (repository.existByTitleAndType(researchTitle,type))
            return new PresentationSaveResponse(researchTitle,"Already Exist!");
        else{
            repository.save(presentation);
            return new PresentationSaveResponse(researchTitle,"Saved!");
        }
    }
}
