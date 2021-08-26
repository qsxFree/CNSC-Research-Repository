package com.cnsc.research.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Table(name = "Research")
@Entity
@Data
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

    @Transient
    @Column(name = "deleted")
    private Byte deleted;

    @Column(name = "datetime_deleted")
    private LocalDateTime datetimeDeleted;

    @Transient
    @Column(name = "datetime_added")
    private LocalDateTime datetimeAdded;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "file_id", referencedColumnName = "file_id")
    private ResearchFile fileIdByResearchFile;

    @ManyToOne(cascade = {CascadeType.MERGE})
    @JoinColumn(name = "funding_agency", referencedColumnName = "agency_id")
    private FundingAgency fundingAgencyByFundingAgency;


    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "research_status", referencedColumnName = "status_id")
    private ResearchStatus researchStatusByResearchStatus;

    @ManyToOne(cascade = {CascadeType.MERGE})
    @JoinColumn(name = "delivery_unit", referencedColumnName = "unit_id")
    private DeliveryUnit deliveryUnitByDeliveryUnit;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "research_researcher_rel",
            joinColumns = @JoinColumn(name = "research_id"),
            inverseJoinColumns = @JoinColumn(name = "researcher_id")
    )
    private Set<Researchers> researchers;


}
