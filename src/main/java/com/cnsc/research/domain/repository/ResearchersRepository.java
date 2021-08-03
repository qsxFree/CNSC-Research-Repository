package com.cnsc.research.domain.repository;

import com.cnsc.research.domain.model.Researchers;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResearchersRepository extends JpaRepository<Researchers,Integer> {
}
