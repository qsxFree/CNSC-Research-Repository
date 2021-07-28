package com.cnsc.research.domain.model;

import lombok.Data;
import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "research_status", schema = "research_repository")
@Data
public class ResearchStatus {

    @Id
    @Column(name = "status_id", nullable = false)
    private Integer statusId;

    @Column(name = "status_type", nullable = false, length = 30)
    private String statusType;

    @OneToMany(mappedBy = "researchStatusByResearchStatus")
    private Collection<Research> researchByStatusId;

}
