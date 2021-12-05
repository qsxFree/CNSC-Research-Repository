package com.cnsc.research.service;

import com.cnsc.research.domain.mapper.ResearchMapper;
import com.cnsc.research.domain.mapper.ResearcherMapper;
import com.cnsc.research.domain.model.Research;
import com.cnsc.research.domain.model.Researchers;
import com.cnsc.research.domain.repository.ResearchRepository;
import com.cnsc.research.domain.repository.ResearchersRepository;
import com.cnsc.research.domain.transaction.ResearchDto;
import com.cnsc.research.domain.transaction.ResearchersDto;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static java.lang.String.format;
import static org.springframework.http.HttpStatus.*;

//TODO To be tested
@Service
public class ResearcherService {
    private final ResearchersRepository repository;
    private final ResearcherMapper mapper;
    private final Logger logger;
    private final ResearchRepository researchRepository;
    private final ResearchMapper researchMapper;

    @Autowired
    public ResearcherService(ResearchersRepository repository, ResearcherMapper mapper, Logger logger, ResearchRepository researchRepository, ResearchMapper researchMapper) {
        this.repository = repository;
        this.logger = logger;
        this.mapper = mapper;
        this.researchRepository = researchRepository;
        this.researchMapper = researchMapper;
    }

    public List<ResearchersDto> getAllResearchers() {
        return mapper.toTransaction(repository.findAll());
    }

    public ResponseEntity saveResearcher(ResearchersDto researchersDto) {
        try {
            if (!repository.existsByNameAndDeletedIsFalseAllIgnoreCase(researchersDto.getResearcherName())) {
                repository.save(mapper.toDomain(researchersDto));
                return new ResponseEntity<String>(format("Researcher %s has successfully added", researchersDto.getResearcherName()), OK);
            } else {
                return new ResponseEntity<String>(format("Researcher %s already exist", researchersDto.getResearcherName()), BAD_REQUEST);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            return new ResponseEntity<String>("Error on saving researcher", INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity updateResearcher(ResearchersDto researchersDto) {
        try {
            if (!repository.existsByNameAndDeletedIsFalseAllIgnoreCase(researchersDto.getResearcherName())) {
                repository.save(mapper.toDomain(researchersDto));
                return new ResponseEntity<String>(format("Researcher %s has successfully updated", researchersDto.getResearcherName()), OK);
            } else {
                return new ResponseEntity<String>(format("Researcher %s already exist", researchersDto.getResearcherName()), BAD_REQUEST);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            return new ResponseEntity<String>("Error on updating researcher", INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity deleteResearcher(Integer researcherId) {
        try {
            long referenceCount = referenceCount(researcherId);
            Optional<Researchers> researchersOptional = repository.findByResearcherIdAndDeletedIsFalse(researcherId);
            if (researchersOptional.isPresent() && referenceCount <= 0) {
                if (researchersOptional.isPresent()) {
                    Researchers researcher = researchersOptional.get();
                    researcher.setDeleted(true);
                    researcher.setDateRemoved(LocalDateTime.now());
                    repository.save(researcher);
                    return new ResponseEntity<String>(format("Researcher %s has successfully deleted", researcher.getName()), OK);
                } else {
                    return new ResponseEntity<String>("Cannot delete researcher", BAD_REQUEST);
                }
            } else {
                return new ResponseEntity<String>("The researcher is referenced to " + referenceCount + " researches.", BAD_REQUEST);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            return new ResponseEntity<String>("Error on deleting researcher", INTERNAL_SERVER_ERROR);
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

    public long referenceCount(Integer id) {
        return researchRepository.countByResearchers_ResearcherId(id);
    }

    public ResponseEntity getResearches(Integer id) {
        try {
            List<Research> research = researchRepository.findByResearchers_ResearcherIdAndDeletedIsFalse(id);
            List<ResearchDto> researchersDtos = researchMapper.toTransaction(research);
            return new ResponseEntity<List<ResearchDto>>(researchersDtos, OK);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            return new ResponseEntity<String>("Can't retrieve researches", INTERNAL_SERVER_ERROR);
        }

    }
}
