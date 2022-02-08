package com.cnsc.research.domain.repository;

import com.cnsc.research.domain.model.PresentationLog;
import com.cnsc.research.domain.transaction.LogDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PresentationLogRepository extends JpaRepository<PresentationLog, Long> {

    @Query("select new com.cnsc.research.domain.transaction.LogDto(pl.logId,u.username,u.name, pl.dateTimeCommit,pl.action,pl.presentationId) " +
            "from PresentationLog pl " +
            "left join User u on pl.userId = u.id " +
            "order by pl.dateTimeCommit desc ")
    List<LogDto> finAllJoined();


    @Query("select new com.cnsc.research.domain.transaction.LogDto(pl.logId,u.username,u.name, pl.dateTimeCommit,pl.action,pl.presentationId) " +
            "from PresentationLog pl " +
            "left join User u on pl.userId = u.id " +
            "where pl.action = 'ADD' and pl.presentationId = ?1" +
            " order by pl.dateTimeCommit desc ")
    Optional<LogDto> addMetadata(Long dataId);


    @Query("select new com.cnsc.research.domain.transaction.LogDto(pl.logId,u.username,u.name, pl.dateTimeCommit,pl.action,pl.presentationId) " +
            "from PresentationLog pl " +
            "left join User u on pl.userId = u.id " +
            "where pl.action = 'EDIT' and pl.presentationId = ?1" +
            " order by pl.dateTimeCommit desc ")
    List<LogDto> editMetadata(Long dataId);

}
