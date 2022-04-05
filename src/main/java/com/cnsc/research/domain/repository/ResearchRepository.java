package com.cnsc.research.domain.repository;

import com.cnsc.research.domain.model.Research;
import com.cnsc.research.domain.model.ResearchStatus;
import com.cnsc.research.domain.model.analysis.BudgetDate;
import com.cnsc.research.domain.model.analysis.StatusCount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
public interface ResearchRepository extends JpaRepository<Research, Integer> {
    @Query("select r from Research r where r.isPublic = true and r.deleted = false")
    List<Research> findByIsPublicIsTrueAndDeletedFalse();

    @Query("select r from Research r where r.deleted = true order by r.datetimeDeleted DESC")
    List<Research> getDeleted();

    @Query("select count(r) from Research r where r.deleted = false")
    long countByDeletedIsFalse();


    Page<Research> findByDeletedFalse(Pageable pageable);

    boolean existsByResearchFile_TitleIgnoreCaseAndDeletedIsFalse(String title);

    @Query("select (count(r) > 0) from Research r where r.researchFile.title = ?1 and r.deleted = false and r.researchId <> ?2")
    boolean researchTitleExistNotMatchingID(String title, Integer researchId);


    @Query("select r from Research r where upper(r.researchFile.title) = upper(?1) and r.deleted = false")
    Optional<Research> findResearchByTitle(String title);

    @Query("select (count(r) > 0) from Research r where upper(r.researchFile.title) = upper(?1) and r.deleted = false")
    boolean findResearchByTitleAndAvailability(String title);

    List<Research> findByResearchFile_TitleIsContainingIgnoreCaseAndDeletedIsFalse(@NonNull String title);


    @Query(value = "select distinct(r) from Research r " +
            " left join r.fundingAgencies fundingAgencies " +
            " left join r.researchers researchers " +
            " left join r.researchAgendaList agendaList" +
            " where  ( r.budget >= :budgetStart and r.budget <= :budgetEnd) " +
            " and (:startDate is null or r.startDate >= :startDate) " +
            " and (:endDate is null or r.endDate <= :endDate) " +
            " and (coalesce(:agencyNames) is null or fundingAgencies.agencyName in (:agencyNames)) " +
            " and (coalesce(:unitNames) is null or r.deliveryUnit.unitName in (:unitNames))" +
            " and (coalesce(:names) is null or researchers.name in (:names)) " +
            " and (coalesce(:agenda) is null or agendaList.agenda in (:agenda)) " +
            " and (coalesce(:researchStatuses) is null or r.researchStatus in (:researchStatuses))" +
            " and r.deleted = false")
    List<Research> findAdvanced(@Param("budgetStart") Double budgetStart,
                                @Param("budgetEnd") Double budgetEnd,
                                @Param("startDate") LocalDate startDate,
                                @Param("endDate") LocalDate endDate,
                                @Param("agencyNames") Collection<String> agencyNames,
                                @Param("unitNames") Collection<String> unitNames,
                                @Param("names") Collection<String> names,
                                @Param("agenda") Collection<String> agenda,
                                @Param("researchStatuses") Collection<ResearchStatus> researchStatuses);


    @Query(value = "select distinct(r) from Research r" +
            " left join r.fundingAgencies fundingAgencies " +
            " left join r.researchers researchers " +
            " left join r.researchAgendaList agendaList" +
            " where r.deliveryUnit.unitId = :unitId " +
            " or fundingAgencies.agencyId = :agencyId " +
            " or researchers.researcherId = :researcherId " +
            " or agendaList.agenda like upper(:agenda) " +
            " or r.researchFile.title like upper(:title)" +
            " and r.deleted = false"
    )
    List<Research> findPublic(@Param("unitId") Integer unitId,
                              @Param("agencyId") Integer agencyId,
                              @Param("researcherId") Integer researcherId,
                              @Param("agenda") String agenda,
                              @Param("title") String title);

    List<Research> findByDeletedIsFalse();

    @Query("select max(r.budget) from Research r")
    Double getMaxBudget();

    long countByDeliveryUnit_UnitId(Integer unitId);

    long countByFundingAgencies_AgencyId(Integer agencyId);

    long countByResearchers_ResearcherId(Integer researcherId);

    List<Research> findByDeletedFalseOrderByStartDateAsc();

    List<Research> findByDeliveryUnit_UnitIdAndDeletedIsFalse(Integer unitId);

    List<Research> findByFundingAgencies_AgencyIdAndDeletedIsFalse(Integer agencyId);

    List<Research> findByResearchers_ResearcherIdAndDeletedIsFalse(Integer researcherId);

    List<Research> findByResearchAgendaList_AgendaIgnoreCaseOrderByResearchFile_TitleAsc(String agenda);

    List<Research> findByResearchStatusAndDeletedIsFalseOrderByResearchFile_TitleAsc(ResearchStatus researchStatus);

    @Query("select new com.cnsc.research.domain.model.analysis.StatusCount(r.researchStatus, count(r.researchStatus)) from Research  r group by r.researchStatus")
    List<StatusCount> getStatusCount();

    @Query("select new com.cnsc.research.domain.model.analysis.BudgetDate(year(r.startDate),sum(r.budget)) from Research r group by year(r.startDate) order by year(r.startDate) asc")
    List<BudgetDate> getBudgetByYear();

    @Query("select r from Research r where year(r.endDate) = ?1 and month(r.endDate) = ?2")
    List<Research> getResearchByEndDate(int year, int month);

    @Query(value = "select * from research where deleted = false order by datetime_added desc limit 10", nativeQuery = true)
    List<Research> getRecentlyAdded();

    @Query(value = "select * from research where deleted = false order by view desc limit 10", nativeQuery = true)
    List<Research> getMostViewed();
}
