package com.cnsc.research.domain.repository;

import com.cnsc.research.domain.model.ResearchFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResearchFileRepository extends JpaRepository<ResearchFile,Long> {
}
