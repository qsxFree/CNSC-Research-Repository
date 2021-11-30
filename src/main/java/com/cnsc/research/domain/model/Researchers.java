package com.cnsc.research.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Researchers {

    @Id
    @Column(name = "researcher_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer researcherId;

    @Column(name = "name", nullable = false, length = 250)
    private String name;

    @Column(name = "date_created", updatable = false, insertable = false)
    private LocalDateTime dateCreated;

    @Column(name = "date_removed")
    private LocalDateTime dateRemoved;

    @Column(name = "deleted", insertable = false)
    private boolean deleted;

    @ManyToMany(mappedBy = "researchers", fetch = FetchType.LAZY)
    private Set<Research> researches;

    @ManyToMany(mappedBy = "researchers", fetch = FetchType.LAZY)
    private List<Publication> publications;
}
