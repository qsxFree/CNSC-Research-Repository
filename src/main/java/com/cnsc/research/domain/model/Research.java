package com.cnsc.research.domain.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Table(name = "Research")
@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Research {
    @Id
    @Column(name = "research_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer researchId;

    @Column(name = "budget", nullable = false, precision = 0)
    private Double budget;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "remarks", length = 300)
    private String remarks;

    @Column(name = "is_public")
    private boolean isPublic;

    @Column(name = "view")
    private Long view;

    @Column(name = "deleted", insertable = false)
    private boolean deleted;

    @Column(name = "datetime_deleted")
    private LocalDateTime datetimeDeleted;

    @Transient
    @Column(name = "datetime_added")
    private LocalDateTime datetimeAdded;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "file_id", referencedColumnName = "file_id")
    private ResearchFile researchFile;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "research_agency_rel",
            joinColumns = @JoinColumn(name = "research_id"),
            inverseJoinColumns = @JoinColumn(name = "agency_id")
    )
    private List<FundingAgency> fundingAgencies;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "agenda_research_rel",
            joinColumns = @JoinColumn(name = "research_id"),
            inverseJoinColumns = @JoinColumn(name = "agenda_id")
    )
    private List<ResearchAgenda> researchAgendaList;

    @Enumerated(EnumType.STRING)
    private ResearchStatus researchStatus;

    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "delivery_unit", referencedColumnName = "unit_id")
    private DeliveryUnit deliveryUnit;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "research_researcher_rel",
            joinColumns = @JoinColumn(name = "research_id"),
            inverseJoinColumns = @JoinColumn(name = "researcher_id")
    )
    private List<Researchers> researchers;

    @OneToMany(mappedBy = "research", fetch = FetchType.LAZY)
    private List<Presentation> presentations;
}
