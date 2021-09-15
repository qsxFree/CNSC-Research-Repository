package com.cnsc.research.domain.repository;

import com.cnsc.research.domain.model.Presentation;
import com.cnsc.research.domain.model.PresentationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PresentationRepository extends JpaRepository<Presentation,Long> {
    Optional<Presentation> findByPresentationIdAndDeletedIsFalse(@NonNull Long presentationId);

    @Query("select (count(p) > 0) from Presentation p where upper(p.research.researchFile.title) = upper(?1) and p.type = ?2 and p.deleted = false")
    boolean existByTitleAndType(String title, PresentationType type);


}
