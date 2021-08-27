package com.cnsc.research.domain.repository;

import com.cnsc.research.domain.model.Research;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResearchRepository extends JpaRepository<Research, Long> {

}
