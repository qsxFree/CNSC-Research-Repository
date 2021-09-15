package com.cnsc.research.domain.mapper;

import com.cnsc.research.domain.model.Presentation;
import com.cnsc.research.domain.model.PresentationType;
import com.cnsc.research.domain.transaction.PresentationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static com.cnsc.research.domain.model.PresentationType.*;
import static com.cnsc.research.domain.model.PresentationType.INTERNATIONAL;

@Component
public class PresentationMapper {

    private final ResearchMapper researchMapper;

    @Autowired
    public PresentationMapper(ResearchMapper researchMapper) {
        this.researchMapper = researchMapper;
    }

    public PresentationDto toPresentationDto(Presentation presentation) {
        return PresentationDto.builder()
                .presentationId(presentation.getPresentationId())
                .presentationDate(presentation.getPresentationDate())
                .research(researchMapper.toResearchDto(presentation.getResearch()))
                .build();
    }

    public List<PresentationDto> toPresentationDto(Collection<Presentation> presentations) {
        return presentations.stream()
                .map(this::toPresentationDto)
                .collect(Collectors.toList());
    }


    public Presentation toPresentation(PresentationDto presentationDto) {
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
                .research(researchMapper.toResearch(presentationDto.getResearch()))
                .build();
    }
}
