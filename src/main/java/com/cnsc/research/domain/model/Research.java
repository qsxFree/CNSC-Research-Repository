package com.cnsc.research.domain.model;

import lombok.Data;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Objects;
import java.util.Set;

@Table(name = "Research")
@Entity
@Data
public class Research {
    @Id
    @Column(name = "research_id", nullable = false)
    private Integer researchId;

    @Column(name = "budget", nullable = false, precision = 0)
    private Integer budget;

    @Column(name = "start_date")
    private Date startDate;

    @Column(name = "end_date")
    private Date endDate;

    @Column(name = "remarks", length = 300)
    private String remarks;

    @Column(name = "deleted", nullable = false)
    private Byte deleted;

    @Column(name = "datetime_deleted")
    private Timestamp datetimeDeleted;

    @Column(name = "datetime_added", nullable = false)
    private Timestamp datetimeAdded;

    @OneToOne
    @JoinColumn(name = "research_file",referencedColumnName = "file_id")
    private ResearchFile file_id;

    @ManyToOne
    @JoinColumn(name = "funding_agency", referencedColumnName = "agency_id")
    private FundingAgency fundingAgencyByFundingAgency;


    @ManyToOne
    @JoinColumn(name = "research_status", referencedColumnName = "status_id")
    private ResearchStatus researchStatusByResearchStatus;

    @ManyToOne
    @JoinColumn(name = "delivery_unit", referencedColumnName = "unit_id")
    private DeliveryUnit deliveryUnitByDeliveryUnit;

    @ManyToMany
    @JoinTable(
            name = "research_researcher_rel",
            joinColumns = @JoinColumn(name = "research_id"),
            inverseJoinColumns = @JoinColumn(name = "researcher_id")
    )
    private Set<Researchers> researchers;


}
