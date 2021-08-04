package com.cnsc.research.domain.repository;

import com.cnsc.research.domain.model.ResearchStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ResearchStatusRepository extends JpaRepository<ResearchStatus,Integer> {
    Optional<ResearchStatus> findByStatusTypeIgnoreCase(String statusType);

}
