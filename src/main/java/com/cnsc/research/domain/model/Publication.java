package com.cnsc.research.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "publication")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Publication {
    @Id
    @Column(name = "publication_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long publicationId;

    @Column(name = "publication_title")
    private String publicationTitle;

    @Column(name = "publication_link")
    private String publicationLink;

    @Transient
    @Column(name = "datetime_added")
    private LocalDateTime dateTimeAdded;

    @Column(name = "datetime_removed")
    private LocalDateTime dateTimeDeleted;

    private boolean deleted;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "publication_researcher_rel",
        joinColumns = @JoinColumn(name = "publication_id"),
            inverseJoinColumns = @JoinColumn(name="researcher_id")
    )
    private List<Researchers> researchers;
}
