package com.cnsc.research.api;

import com.cnsc.research.domain.transaction.FundingAgencyDto;
import com.cnsc.research.service.FundingAgencyService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public List<FundingAgencyDto> getAllFundingAgencies() {
        return service.getAllFundingAgencies();
    }

    @PostMapping
    public ResponseEntity addFundingAgency(@RequestBody FundingAgencyDto fundingAgencyDto) {
        return service.addFundingAgency(fundingAgencyDto);
    }

    @PutMapping
    public ResponseEntity editDeliveryUnit(@RequestBody FundingAgencyDto fundingAgencyDto) {
        return service.editFundingAgency(fundingAgencyDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteDeliveryUnit(@PathVariable(name = "id") Integer id) {
        return service.deleteFundingAgency(id);
    }

    @GetMapping("/{id}")
    public ResponseEntity getDeliveryUnit(@PathVariable(name = "id") Integer id) {
        return service.getFundingAgencyById(id);
    }

    @GetMapping("/{id}/research/list")
    public ResponseEntity getResearches(@PathVariable(name = "id") Integer id) {
        return service.getResearches(id);
    }
}
