package com.cnsc.research.domain.repository;

import com.cnsc.research.domain.model.Researchers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Optional;

public interface ResearchersRepository extends JpaRepository<Researchers, Integer> {
    Optional<Researchers> findByNameIgnoreCase(@NonNull String name);

    @Query("select r from Researchers r where upper(r.name) = upper(?1) and r.deleted = false")
    Optional<Researchers> findByNameIgnoreCaseAndNotDeleted(String name);

    boolean existsByNameAndDeletedIsFalseAllIgnoreCase(String name);

    Optional<Researchers> findByResearcherIdAndDeletedIsFalse(Integer researcherId);

    List<Researchers> findByDeletedFalseOrderByNameAsc();


}
