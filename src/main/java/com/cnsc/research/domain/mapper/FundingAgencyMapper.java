package com.cnsc.research.domain.mapper;

import com.cnsc.research.domain.model.FundingAgency;
import com.cnsc.research.domain.transaction.FundingAgencyDto;
import org.springframework.stereotype.Component;

@Component
public class FundingAgencyMapper extends GeneralMapper<FundingAgency, FundingAgencyDto> {
    @Override
    public FundingAgency toDomain(FundingAgencyDto transactionsData) throws Exception {
        return new FundingAgency(transactionsData.getAgencyId(), transactionsData.getAgencyName(), null);
    }

    public FundingAgencyDto toTransaction(FundingAgency fundingAgency) {
        return new FundingAgencyDto(fundingAgency.getAgencyId(), fundingAgency.getAgencyName());
    }


}
