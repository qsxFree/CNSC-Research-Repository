package com.cnsc.research.service;

import com.cnsc.research.domain.mapper.DeliveryUnitMapper;
import com.cnsc.research.domain.repository.DeliveryUnitRepository;
import com.cnsc.research.domain.transaction.DeliveryUnitDto;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeliveryUnitService {
    public final DeliveryUnitRepository deliveryUnitRepository;
    public final Logger logger;
    public final DeliveryUnitMapper mapper;

    @Autowired
    public DeliveryUnitService(DeliveryUnitRepository deliveryUnitRepository, Logger logger, DeliveryUnitMapper mapper) {
        this.deliveryUnitRepository = deliveryUnitRepository;
        this.logger = logger;
        this.mapper = mapper;
    }

    public List<DeliveryUnitDto> getDistinctDeliveryUnitName(){
        return mapper.toTransaction(deliveryUnitRepository.findDistinctByOrderByUnitNameAsc());
    }
}
