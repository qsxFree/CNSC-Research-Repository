package com.cnsc.research.domain.repository;

import com.cnsc.research.domain.model.ResearchFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ResearchFileRepository extends JpaRepository<ResearchFile, Long> {
    Optional<ResearchFile> findByTitleIgnoreCase(String title);

    @Query("select r from ResearchFile r where upper(r.title) = upper(?1) and r.deleted = false")
    Optional<ResearchFile> findByResearchTitleAndAvailabiity(String title);

    @Query("select r from ResearchFile r where r.research.deleted = false")
    List<ResearchFile> getTitles();


}
