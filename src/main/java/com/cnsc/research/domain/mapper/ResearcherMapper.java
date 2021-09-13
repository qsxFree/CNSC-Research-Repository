package com.cnsc.research.domain.mapper;

import com.cnsc.research.domain.model.Researchers;
import com.cnsc.research.domain.transaction.ResearchersDto;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ResearcherMapper {
    
    public  ResearchersDto toResearcherDto(Researchers researcher){
        return new ResearchersDto(researcher.getResearcherId(),researcher.getName());
    }

    public  List<ResearchersDto> toResearchDto(Collection<Researchers> researchers){
        return researchers.stream()
                .map(this::toResearcherDto)
                .collect(Collectors.toList());
    }

    public  Researchers toResearcher(ResearchersDto researchersDto){
        Researchers researchers = Researchers.builder()
                .researcherId(researchersDto.getResearcherId())
                .name(researchersDto.getResearcherName())
                .build();
        return researchers;
    }

    public  List<Researchers> toResearcher(Collection<ResearchersDto> researchersDtos){
        return researchersDtos.stream()
                .map(this::toResearcher)
                .collect(Collectors.toList());
    }
}
