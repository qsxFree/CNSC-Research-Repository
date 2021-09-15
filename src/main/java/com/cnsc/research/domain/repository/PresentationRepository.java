package com.cnsc.research.domain.repository;

import com.cnsc.research.domain.model.Presentation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PresentationRepository extends JpaRepository<Presentation,Long> {

}
