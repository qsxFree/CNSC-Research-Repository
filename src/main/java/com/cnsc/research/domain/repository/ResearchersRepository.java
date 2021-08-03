package com.cnsc.research.domain.repository;

import com.cnsc.research.domain.model.Researchers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import java.util.Optional;

public interface ResearchersRepository extends JpaRepository<Researchers,Integer> {
    Optional<Researchers> findByNameIgnoreCase(@NonNull String name);
}
