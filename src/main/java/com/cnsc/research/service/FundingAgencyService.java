package com.cnsc.research.service;

import com.cnsc.research.domain.mapper.FundingAgencyMapper;
import com.cnsc.research.domain.mapper.ResearchMapper;
import com.cnsc.research.domain.model.FundingAgency;
import com.cnsc.research.domain.model.Research;
import com.cnsc.research.domain.repository.FundingAgencyRepository;
import com.cnsc.research.domain.repository.ResearchRepository;
import com.cnsc.research.domain.transaction.FundingAgencyDto;
import com.cnsc.research.domain.transaction.ResearchDto;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static java.lang.String.format;
import static org.springframework.http.HttpStatus.*;

@Service
public class FundingAgencyService {
    private final FundingAgencyRepository repository;
    private final Logger logger;
    private final FundingAgencyMapper mapper;
    private final ResearchMapper researchMapper;
    private final ResearchRepository researchRepository;

    @Autowired
    public FundingAgencyService(FundingAgencyRepository repository, Logger logger, FundingAgencyMapper mapper, ResearchMapper researchMapper, ResearchRepository researchRepository) {
        this.repository = repository;
        this.logger = logger;
        this.mapper = mapper;
        this.researchMapper = researchMapper;
        this.researchRepository = researchRepository;
    }

    public List<FundingAgencyDto> getAllFundingAgencies() {
        return mapper.toTransaction(repository.findAll());
    }

    public ResponseEntity addFundingAgency(FundingAgencyDto fundingAgencyDto) {
        Optional<FundingAgency> fundingAgencyOptional = repository
                .findByAgencyNameIgnoreCase(fundingAgencyDto.getAgencyName());

        if (fundingAgencyOptional.isEmpty()) {
            try {
                repository.save(mapper.toDomain(fundingAgencyDto));
                return new ResponseEntity<String>(format("%s has successfully added", fundingAgencyDto.getAgencyName()), CREATED);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error(e.getMessage());
                return new ResponseEntity<String>(format("error on saving %s", fundingAgencyDto.getAgencyName()), INTERNAL_SERVER_ERROR);
            }
        } else {
            return new ResponseEntity<String>(format("%s already exist", fundingAgencyDto.getAgencyName()), BAD_REQUEST);
        }
    }

    public ResponseEntity editFundingAgency(FundingAgencyDto fundingAgencyDto) {
        Optional<FundingAgency> fundingAgencyOptional = repository
                .findByAgencyNameIgnoreCase(fundingAgencyDto.getAgencyName());

        if (fundingAgencyOptional.isEmpty()) {
            try {
                repository.save(mapper.toDomain(fundingAgencyDto));
                return new ResponseEntity<String>(format("%s has successfully edited", fundingAgencyDto.getAgencyName()), CREATED);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error(e.getMessage());
                return new ResponseEntity<String>(format("error on editing %s", fundingAgencyDto.getAgencyName()), INTERNAL_SERVER_ERROR);
            }
        } else {
            return new ResponseEntity<String>(format("%s already exist", fundingAgencyDto.getAgencyName()), BAD_REQUEST);
        }
    }

    public ResponseEntity getFundingAgencyById(Integer id) {
        try {
            Optional<FundingAgency> fundingAgencyOptional = repository.findById(id);
            if (fundingAgencyOptional.isPresent()) {
                return new ResponseEntity<FundingAgencyDto>(mapper.toTransaction(fundingAgencyOptional.get()), OK);
            } else {
                return new ResponseEntity<String>("Can't retrieve funding agency", BAD_REQUEST);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            return new ResponseEntity<String>("Can't retrieve funding agency", INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity deleteFundingAgency(Integer id) {
        try {
            long referenceCount = referenceCount(id);
            if (referenceCount <= 0) {
                repository.deleteById(id);
                return new ResponseEntity<String>("Delete success", OK);
            } else {
                return new ResponseEntity<String>("The funding agency is referenced to " + referenceCount + " researches.", BAD_REQUEST);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            return new ResponseEntity<String>("Error on deleting funding agency", INTERNAL_SERVER_ERROR);
        }
    }

    public long referenceCount(Integer id) {
        return researchRepository.countByFundingAgencies_AgencyId(id);
    }

    public ResponseEntity getResearches(Integer id) {

        try {
            List<Research> research = researchRepository.findByFundingAgencies_AgencyIdAndDeletedIsFalse(id);
            List<ResearchDto> researchersDtos = researchMapper.toTransaction(research);
            return new ResponseEntity<List<ResearchDto>>(researchersDtos, OK);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            return new ResponseEntity<String>("Can't retrieve researches", INTERNAL_SERVER_ERROR);
        }

    }

    public ResponseEntity getDistinctFundingAgency() {
        try {
            return new ResponseEntity(mapper.toTransaction(repository.findByOrderByAgencyNameAsc()), OK);
        } catch (Exception e) {
            return new ResponseEntity("Error on retrieving Funding Agency", INTERNAL_SERVER_ERROR);
        }
    }
}
