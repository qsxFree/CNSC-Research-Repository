package com.cnsc.research.domain.mapper;

import com.cnsc.research.domain.model.Researchers;
import com.cnsc.research.domain.transaction.ResearchersDto;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ResearcherMapper {
    public static ResearchersDto toResearcherDto(Researchers researcher){
        return new ResearchersDto(researcher.getResearcherId(),researcher.getName());
    }

    public static List<ResearchersDto> toResearchDto(Collection<Researchers> researchers){
        return researchers.stream()
                .map(ResearcherMapper::toResearcherDto)
                .collect(Collectors.toList());
    }
}
