package com.cnsc.research.domain.repository;

import com.cnsc.research.domain.model.DataLog;
import com.cnsc.research.domain.transaction.LogDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DataLogRepository extends JpaRepository<DataLog, Long> {

    @Query("select new com.cnsc.research.domain.transaction.LogDto(dl.logId, u.username,u.name, dl.dateTimeCommit,dl.action,dl.dataId) " +
            "from DataLog dl " +
            "left join User u on dl.userId = u.id " +
            "order by dl.dateTimeCommit desc ")
    List<LogDto> getAllLogs();

    @Query("select new com.cnsc.research.domain.transaction.LogDto(dl.logId, u.username,u.name, dl.dateTimeCommit,dl.action,dl.dataId) " +
            "from DataLog dl " +
            "left join User u on dl.userId = u.id " +
            "where dl.entityType = 'RESEARCH' " +
            "order by dl.dateTimeCommit desc ")
    List<LogDto> getAllResearchLog();

    @Query("select new com.cnsc.research.domain.transaction.LogDto(dl.logId, u.username,u.name, dl.dateTimeCommit,dl.action,dl.dataId) " +
            "from DataLog dl " +
            "left join User u on dl.userId = u.id " +
            "where dl.entityType = 'PRESENTATION' " +
            "order by dl.dateTimeCommit desc ")
    List<LogDto> getAllPresentationLog();

    @Query("select new com.cnsc.research.domain.transaction.LogDto(dl.logId, u.username,u.name, dl.dateTimeCommit,dl.action,dl.dataId) " +
            "from DataLog dl " +
            "left join User u on dl.userId = u.id " +
            "where dl.entityType = 'PUBLICATION' " +
            "order by dl.dateTimeCommit desc ")
    List<LogDto> getAllPublicationLog();


    @Query("select new com.cnsc.research.domain.transaction.LogDto(dl.logId,u.username,u.name, dl.dateTimeCommit,dl.action,dl.dataId) " +
            "from DataLog dl " +
            "left join User u on dl.userId = u.id " +
            "where dl.entityType = 'RESEARCH' and dl.action = 'ADD' and dl.dataId = ?1")
    Optional<LogDto> addResearchMetadata(Long dataId);


    @Query("select new com.cnsc.research.domain.transaction.LogDto(dl.logId,u.username,u.name, dl.dateTimeCommit,dl.action,dl.dataId) " +
            "from DataLog dl " +
            "left join User u on dl.userId = u.id " +
            "where dl.entityType = 'RESEARCH' and dl.action = 'EDIT' and dl.dataId = ?1" +
            " order by dl.dateTimeCommit desc ")
    List<LogDto> editResearchMetadata(Long dataId);


    @Query("select new com.cnsc.research.domain.transaction.LogDto(dl.logId,u.username,u.name, dl.dateTimeCommit,dl.action,dl.dataId) " +
            "from DataLog dl " +
            "left join User u on dl.userId = u.id " +
            "where dl.entityType = 'PRESENTATION' and dl.action = 'ADD' and dl.dataId = ?1")
    Optional<LogDto> addPresentationMetadata(Long dataId);

    @Query("select new com.cnsc.research.domain.transaction.LogDto(dl.logId,u.username,u.name, dl.dateTimeCommit,dl.action,dl.dataId) " +
            "from DataLog dl " +
            "left join User u on dl.userId = u.id " +
            "where dl.entityType = 'PRESENTATION' and dl.action = 'EDIT' and dl.dataId = ?1" +
            " order by dl.dateTimeCommit desc ")
    List<LogDto> editPresentationMetadata(Long dataId);

    @Query("select new com.cnsc.research.domain.transaction.LogDto(dl.logId,u.username,u.name, dl.dateTimeCommit,dl.action,dl.dataId) " +
            "from DataLog dl " +
            "left join User u on dl.userId = u.id " +
            "where dl.entityType = 'PUBLICATION' and dl.action = 'ADD' and dl.dataId = ?1")
    Optional<LogDto> addPublicationMetadata(Long dataId);

    @Query("select new com.cnsc.research.domain.transaction.LogDto(dl.logId,u.username,u.name, dl.dateTimeCommit,dl.action,dl.dataId) " +
            "from DataLog dl " +
            "left join User u on dl.userId = u.id " +
            "where dl.entityType = 'PUBLICATION' and dl.action = 'EDIT' and dl.dataId = ?1" +
            " order by dl.dateTimeCommit desc ")
    List<LogDto> editPublicationMetadata(Long dataId);
}
