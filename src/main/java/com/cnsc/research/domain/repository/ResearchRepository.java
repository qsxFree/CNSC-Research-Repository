package com.cnsc.research.domain.repository;

import com.cnsc.research.domain.model.Research;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ResearchRepository extends PagingAndSortingRepository<Research, Integer> {
    Optional<Research> findByResearchFile_TitleIgnoreCase(@NonNull String title);

    Page<Research> findByDeletedFalse(Pageable pageable);

    @Query("select r from Research r where upper(r.researchFile.title) = upper(?1) and r.deleted = false")
    Optional<Research> findResearchByTitle(String title);

}
