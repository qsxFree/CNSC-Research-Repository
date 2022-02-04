package com.cnsc.research.domain.repository;

import com.cnsc.research.domain.model.PresentationLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PresentationLogRepository extends JpaRepository<PresentationLog, Long> {

}
