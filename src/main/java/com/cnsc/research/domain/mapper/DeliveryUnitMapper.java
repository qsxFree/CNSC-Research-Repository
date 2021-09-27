package com.cnsc.research.domain.mapper;

import com.cnsc.research.domain.model.DeliveryUnit;
import com.cnsc.research.domain.transaction.DeliveryUnitDto;
import org.springframework.stereotype.Component;

@Component
public class DeliveryUnitMapper extends GeneralMapper<DeliveryUnit, DeliveryUnitDto> {

    public DeliveryUnitDto toTransaction(DeliveryUnit deliveryUnit) {
        return new DeliveryUnitDto(deliveryUnit.getUnitId(), deliveryUnit.getUnitName());
    }

    @Override
    public DeliveryUnit toDomain(DeliveryUnitDto transactionsData) throws Exception {
        return null;
    }
}
