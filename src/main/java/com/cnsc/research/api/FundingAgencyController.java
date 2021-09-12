package com.cnsc.research.api;

import com.cnsc.research.domain.transaction.FundingAgencyDto;
import com.cnsc.research.service.FundingAgencyService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/funding-agency")
public class FundingAgencyController {
    private final Logger logger;
    private final FundingAgencyService service;

    @Autowired
    public FundingAgencyController(Logger logger, FundingAgencyService service) {
        this.logger = logger;
        this.service = service;
    }

    @GetMapping("/list")
    public List<FundingAgencyDto> getAllFundingAgencies(){
        return service.getAllFundingAgencies();
    }
}
