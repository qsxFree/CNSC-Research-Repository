package com.cnsc.research.domain.repository;

import com.cnsc.research.domain.model.ResearchAgenda;
import com.cnsc.research.domain.model.analysis.AgendaCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResearchAgendaRepository extends JpaRepository<ResearchAgenda, Long> {
    @Query("select  distinct r.agenda from ResearchAgenda r order by r.agenda")
    List<String> findDistinct();

    @Query("Select new com.cnsc.research.domain.model.analysis.AgendaCount(r.agenda ,count(r.agenda)) from ResearchAgenda r group by r.agenda")
    List<AgendaCount> retrieveAgendaCount();
}
