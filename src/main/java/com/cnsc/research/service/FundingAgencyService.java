package com.cnsc.research.service;

import com.cnsc.research.domain.mapper.FundingAgencyMapper;
import com.cnsc.research.domain.repository.FundingAgencyRepository;
import com.cnsc.research.domain.transaction.FundingAgencyDto;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FundingAgencyService {
    private final FundingAgencyRepository repository;
    private final Logger logger;

    @Autowired
    public FundingAgencyService(FundingAgencyRepository repository, Logger logger) {
        this.repository = repository;
        this.logger = logger;
    }

    public List<FundingAgencyDto> getAllFundingAgencies() {
        return FundingAgencyMapper.toFundingAgencyDto(repository.findAll());
    }
}
