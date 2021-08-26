package com.cnsc.research.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "research_status")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResearchStatus {

    @Id
    @Column(name = "status_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer statusId;

    @Column(name = "status_type", nullable = false, length = 30)
    private String statusType;

    @OneToMany(mappedBy = "researchStatusByResearchStatus", fetch = FetchType.LAZY)
    private Collection<Research> researchByStatusId;

}
