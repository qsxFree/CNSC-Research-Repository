package com.cnsc.research.domain.model;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
@Entity
@Table(name = "research_agenda")
public class ResearchAgenda {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "agenda_id")
    private Integer agendaId;

    private String agenda;

    @ManyToMany(mappedBy = "researchAgendaList", fetch = FetchType.EAGER)
    private List<Research> researchList;
}
