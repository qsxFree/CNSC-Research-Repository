package com.cnsc.research.domain.mapper;

import com.cnsc.research.domain.model.Presentation;
import com.cnsc.research.domain.model.PresentationType;
import com.cnsc.research.domain.transaction.PresentationDto;
import com.cnsc.research.misc.EntityBuilders;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static com.cnsc.research.domain.model.PresentationType.*;

@Component
public class PresentationMapper {

    private final EntityBuilders entityBuilders;
    private final ResearcherMapper researcherMapper;
    private final Logger logger;

    @Autowired
    public PresentationMapper(EntityBuilders entityBuilders, ResearcherMapper researcherMapper, Logger logger) {
        this.entityBuilders = entityBuilders;
        this.researcherMapper = researcherMapper;
        this.logger = logger;
    }

    public PresentationDto toPresentationDto(Presentation presentation) {
        return PresentationDto.builder()
                .presentationId(presentation.getPresentationId())
                .presentationDate(presentation.getPresentationDate())
                .presentationType(presentation.getType().name().toLowerCase())
                .presentationTitle(presentation.getResearch().getResearchFile().getTitle())
                .researchers(researcherMapper.toResearchDto(presentation.getResearch().getResearchers()))
                .build();
    }

    public List<PresentationDto> toPresentationDto(Collection<Presentation> presentations) {
        return presentations.stream()
                .map(this::toPresentationDto)
                .collect(Collectors.toList());
    }


    public Presentation toPresentation(PresentationDto presentationDto) throws Exception {
        PresentationType type = null;
        switch (presentationDto.getPresentationType().toLowerCase()) {
            case "local":
                type = LOCAL;
                break;
            case "regional":
                type = REGIONAL;
                break;
            case "national":
                type = NATIONAL;
                break;
            case "international":
                type = INTERNATIONAL;
                break;
            default:
        }
        return Presentation.builder()
                .presentationId(presentationDto.getPresentationId())
                .type(type)
                .presentationDate(presentationDto.getPresentationDate())
                .research(entityBuilders.buildResearchFromDb(presentationDto.getPresentationTitle()))
                .build();
    }

    public List<Presentation> toPresentation(Collection<PresentationDto> presentationDtos){
        return presentationDtos
                .stream()
                .map(item -> {
                    try {
                        return toPresentation(item);
                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }
                })
                .collect(Collectors.toList());
    }
}
