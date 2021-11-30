package com.cnsc.research.service;

import com.cnsc.research.domain.mapper.ResearcherMapper;
import com.cnsc.research.domain.model.Researchers;
import com.cnsc.research.domain.repository.ResearchersRepository;
import com.cnsc.research.domain.transaction.ResearchersDto;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

//TODO To be tested
@Service
public class ResearcherService {
    private final ResearchersRepository repository;
    private final ResearcherMapper mapper;
    private final Logger logger;

    @Autowired
    public ResearcherService(ResearchersRepository repository, ResearcherMapper mapper, Logger logger) {
        this.repository = repository;
        this.logger = logger;
        this.mapper = mapper;
    }

    public List<ResearchersDto> getAllResearchers() {
        return mapper.toTransaction(repository.findAll());
    }

    public String saveResearcher(ResearchersDto researchersDto) {
        try {
            if (!repository.existsByNameAndDeletedIsFalseAllIgnoreCase(researchersDto.getResearcherName())) {
                repository.save(mapper.toDomain(researchersDto));
                return String.format("Researcher %s has successfully added", researchersDto.getResearcherName());
            } else {
                return String.format("Researcher %s already exist", researchersDto.getResearcherName());
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            return "Error on saving researcher";
        }
    }

    public String updateResearcher(ResearchersDto researchersDto) {
        try {
            if (!repository.existsByNameAndDeletedIsFalseAllIgnoreCase(researchersDto.getResearcherName())) {
                repository.save(mapper.toDomain(researchersDto));
                return String.format("Researcher %s has successfully updated", researchersDto.getResearcherName());
            } else {
                return String.format("Researcher %s already exist", researchersDto.getResearcherName());
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            return "Error on updating researcher";
        }
    }

    public String deleteResearcher(Integer researcherId) {
        try {
            Optional<Researchers> researchersOptional = repository.findByResearcherIdAndDeletedIsFalse(researcherId);
            if (researchersOptional.isPresent()) {
                Researchers researcher = researchersOptional.get();
                researcher.setDeleted(true);
                researcher.setDateRemoved(LocalDateTime.now());
                repository.save(researcher);
                return String.format("Researcher %s has successfully deleted", researcher.getName());
            } else {
                return "Cannot find researcher";
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            return "Error on deleting researcher";
        }
    }

    public ResponseEntity getResearcher(Integer researcherId) {
        try {
            Optional<Researchers> result = repository.findByResearcherIdAndDeletedIsFalse(researcherId);
            if (result.isPresent()) {
                return new ResponseEntity<ResearchersDto>(mapper.toTransaction(result.get()), HttpStatus.OK);
            } else {
                return new ResponseEntity<String>("Cannot find researcher", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<String>("Error on retrieving researcher", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
