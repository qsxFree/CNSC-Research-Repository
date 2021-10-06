package com.cnsc.research.domain.mapper;

import com.cnsc.research.domain.model.Researchers;
import com.cnsc.research.domain.transaction.ResearchersDto;
import org.springframework.stereotype.Component;

@Component
public class ResearcherMapper extends GeneralMapper<Researchers, ResearchersDto> {

    public ResearchersDto toTransaction(Researchers researcher) {
        return new ResearchersDto(researcher.getResearcherId(), researcher.getName());
    }

    public Researchers toDomain(ResearchersDto researchersDto) {
        Researchers researchers = Researchers.builder()
                .researcherId(researchersDto.getResearcherId())
                .name(researchersDto.getResearcherName())
                .build();
        return researchers;
    }
}
