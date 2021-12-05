package com.cnsc.research.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "presentation")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Presentation {

    @Id
    @Column(name = "presentation_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long presentationId;

    @Enumerated(EnumType.STRING)
    private PresentationType type;

    @Column(name = "presentation_date")
    private LocalDate presentationDate;

    @Column(name = "is_private")
    private boolean isPrivate;

    @Column(name = "event_name")
    private String eventName;

    @Column(name = "organizing_agency")
    private String organizingAgency;

    @Column(name = "deleted")
    private boolean deleted;

    @Column(name = "datetime_added", insertable = false, updatable = false)
    private LocalDateTime dateTimeAdded;

    @Column(name = "datetime_removed", insertable = false)
    private LocalDateTime dateTimeDeleted;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "research_id", referencedColumnName = "research_id")
    private Research research;
}
