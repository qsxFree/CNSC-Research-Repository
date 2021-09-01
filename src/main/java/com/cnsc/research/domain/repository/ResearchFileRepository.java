package com.cnsc.research.domain.repository;

import com.cnsc.research.domain.model.ResearchFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ResearchFileRepository extends JpaRepository<ResearchFile, Long> {
    Optional<ResearchFile> findByTitleIgnoreCase(String title);

}
