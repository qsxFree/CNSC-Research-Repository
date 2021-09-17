package com.cnsc.research.domain.repository;

import com.cnsc.research.domain.model.Publication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PublicationRepository extends JpaRepository<Publication,Long> {
    List<Publication> findByDeletedIsFalse();

    Optional<Publication> findByPublicationTitleIgnoreCaseAndDeleted(@NonNull String publicationTitle, boolean deleted);



}

