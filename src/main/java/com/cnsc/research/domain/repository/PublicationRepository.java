package com.cnsc.research.domain.repository;

import com.cnsc.research.domain.model.Publication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface PublicationRepository extends JpaRepository<Publication, Long> {
    List<Publication> findByDeletedIsFalse();

    Optional<Publication> findByPublicationTitleIgnoreCaseAndDeleted(@NonNull String publicationTitle, boolean deleted);

    @Query("select p from Publication p where upper(p.publicationTitle) like upper(concat('%', ?1, '%')) and p.deleted = false")
    List<Publication> findByPublicationTitleIsContainingIgnoreCaseAndDeletedIsFalse(String publicationTitle);

    @Query("select p from Publication p " +
            "left join p.researchers researchers " +
            "where (coalesce(:names) is null or  researchers.name in :names) " +
            "and p.deleted = false")
    List<Publication> findAdvanced(@Param("names") Collection<String> names);

    List<Publication> findByResearchers_ResearcherIdIsAndDeletedIsFalse(Integer researcherId);


}
