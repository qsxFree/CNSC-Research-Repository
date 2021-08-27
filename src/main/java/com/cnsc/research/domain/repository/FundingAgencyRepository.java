package com.cnsc.research.domain.repository;

import com.cnsc.research.domain.model.FundingAgency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FundingAgencyRepository extends JpaRepository<FundingAgency,Integer> {
    Optional<FundingAgency> findByAgencyNameIgnoreCase(String agencyName);

}
