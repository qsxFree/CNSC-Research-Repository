package com.cnsc.research.domain.repository;

import com.cnsc.research.domain.model.ResearchStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResearchStatusRepository extends JpaRepository<ResearchStatus,Integer> {
}
