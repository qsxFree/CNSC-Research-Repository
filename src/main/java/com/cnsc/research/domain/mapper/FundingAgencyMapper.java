package com.cnsc.research.domain.mapper;

import com.cnsc.research.domain.model.FundingAgency;
import com.cnsc.research.domain.transaction.FundingAgencyDto;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class FundingAgencyMapper {
    public static FundingAgencyDto toFundingAgencyDto(FundingAgency fundingAgency){
        return new FundingAgencyDto(fundingAgency.getAgencyId(), fundingAgency.getAgencyName());
    }

    public static List<FundingAgencyDto> toFundingAgencyDto(Collection<FundingAgency> fundingAgencies){
        return fundingAgencies.stream().map(FundingAgencyMapper::toFundingAgencyDto)
                .collect(Collectors.toList());
    }
}
