package com.cnsc.research.domain.mapper;

import com.cnsc.research.domain.model.ResearchAgenda;
import com.cnsc.research.domain.transaction.ResearchAgendaDto;
import org.springframework.stereotype.Component;

@Component
public class ResearchAgendaMapper extends GeneralMapper<ResearchAgenda, ResearchAgendaDto> {
    @Override
    public ResearchAgenda toDomain(ResearchAgendaDto transactionsData) throws Exception {
        return new ResearchAgenda(transactionsData.getAgendaId(), transactionsData.getAgendaName(), null);
    }

    @Override
    public ResearchAgendaDto toTransaction(ResearchAgenda domainData) {
        return new ResearchAgendaDto(domainData.getAgendaId(), domainData.getAgenda());
    }
}
