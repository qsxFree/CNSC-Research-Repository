package com.cnsc.research.domain.model;

import lombok.Data;
import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "research_status")
@Data
public class ResearchStatus {

    @Id
    @Column(name = "status_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer statusId;

    @Column(name = "status_type", nullable = false, length = 30)
    private String statusType;

    @OneToMany(mappedBy = "researchStatusByResearchStatus")
    private Collection<Research> researchByStatusId;

}
