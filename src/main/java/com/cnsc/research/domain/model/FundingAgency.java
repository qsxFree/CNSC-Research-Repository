package com.cnsc.research.domain.model;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "funding_agency")
@Data
@Builder
@NoArgsConstructor
public class FundingAgency {

    @Id
    @Column(name = "agency_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer agencyId;

    @Column(name = "agency_name", nullable = false, length = 250)
    private String agencyName;

    @OneToMany(mappedBy = "fundingAgencyByFundingAgency")
    private Set<Research> researchByAgencyId;

}
