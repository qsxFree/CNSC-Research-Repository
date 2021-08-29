package com.cnsc.research.domain.repository;

import com.cnsc.research.domain.model.Research;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ResearchRepository extends PagingAndSortingRepository<Research, Long> {
    Optional<Research> findByResearchFile_TitleIgnoreCase(@NonNull String title);


}
