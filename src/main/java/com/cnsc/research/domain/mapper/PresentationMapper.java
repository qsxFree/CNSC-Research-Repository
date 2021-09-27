package com.cnsc.research.domain.mapper;

import com.cnsc.research.domain.model.Presentation;
import com.cnsc.research.domain.model.PresentationType;
import com.cnsc.research.domain.transaction.PresentationDto;
import com.cnsc.research.misc.EntityBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.cnsc.research.domain.model.PresentationType.*;

@Component
public class PresentationMapper extends GeneralMapper<Presentation, PresentationDto> {

    private final EntityBuilders entityBuilders;
    private final ResearcherMapper researcherMapper;

    @Autowired
    public PresentationMapper(EntityBuilders entityBuilders, ResearcherMapper researcherMapper) {
        this.entityBuilders = entityBuilders;
        this.researcherMapper = researcherMapper;
    }

    public PresentationDto toTransaction(Presentation presentation) {
        return PresentationDto.builder()
                .presentationId(presentation.getPresentationId())
                .presentationDate(presentation.getPresentationDate())
                .presentationType(presentation.getType().name().toLowerCase())
                .presentationTitle(presentation.getResearch().getResearchFile().getTitle())
                .researchers(researcherMapper.toTransaction(presentation.getResearch().getResearchers()))
                .build();
    }


    public Presentation toDomain(PresentationDto presentationDto) throws Exception {
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

}
