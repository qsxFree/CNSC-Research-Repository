package com.cnsc.research.domain.model;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Entity
@Data
@Builder
@NoArgsConstructor
public class Researchers {

    @Id
    @Column(name = "researcher_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer researcherId;

    @Column(name = "name", nullable = false, length = 250)
    private String name;

    @ManyToMany(mappedBy = "researchers")
    private Set<Research> researches;
}
