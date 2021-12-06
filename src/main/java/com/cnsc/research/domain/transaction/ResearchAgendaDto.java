package com.cnsc.research.domain.transaction;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResearchAgendaDto {
    private int agendaId;
    private String agendaName;
}
