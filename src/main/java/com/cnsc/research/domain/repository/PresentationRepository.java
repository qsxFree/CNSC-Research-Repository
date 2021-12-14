package com.cnsc.research.domain.repository;

import com.cnsc.research.domain.model.Presentation;
import com.cnsc.research.domain.model.PresentationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface PresentationRepository extends JpaRepository<Presentation, Long> {
    Optional<Presentation> findByPresentationIdAndDeletedIsFalse(@NonNull Long presentationId);

    @Query("select (count(p) > 0) from Presentation p where upper(p.research.researchFile.title) = upper(?1) and p.type = ?2 and p.deleted = false")
    boolean existByTitleAndType(String title, PresentationType type);

    List<Presentation> findByDeletedIs(boolean deleted);

    @Query("select p from Presentation p where upper(p.research.researchFile.title) like upper(concat('%', ?1, '%')) and p.deleted = false")
    List<Presentation> findByResearch_ResearchFile_TitleIsLikeIgnoreCaseAndDeletedIsFalse(String title);

    @Query("select p from Presentation p " +
            "left join p.research.researchers researchers " +
            "where (coalesce(:names) is null or researchers.name in :names) " +
            "and ((:presentationDateStart is null or :presentationDateEnd is null) " +
            "or p.presentationDate between :presentationDateStart and :presentationDateEnd) " +
            "and (coalesce(:types) is null or p.type in :types) " +
            "and p.deleted = false")
    List<Presentation> findAdvanced(
            @Param("names") Collection<String> names,
            @Param("presentationDateStart") LocalDate presentationDateStart,
            @Param("presentationDateEnd") LocalDate presentationDateEnd,
            @Param("types") Collection<PresentationType> types);

    List<Presentation> findByTypeAndDeletedFalseOrderByResearch_ResearchFile_TitleAsc(PresentationType type);

    List<Presentation> findByResearch_Researchers_ResearcherIdAndDeletedIsFalseOrderByResearch_ResearchFile_FileNameAsc(Integer researcherId);


}
