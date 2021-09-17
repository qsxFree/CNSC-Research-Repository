package com.cnsc.research.domain.mapper;

import com.cnsc.research.domain.model.DeliveryUnit;
import com.cnsc.research.domain.transaction.DeliveryUnitDto;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class DeliveryUnitMapper {
    public static DeliveryUnitDto toDeliveryUnitDto(DeliveryUnit deliveryUnit){
        return new DeliveryUnitDto(deliveryUnit.getUnitId(),deliveryUnit.getUnitName());
    }

    public static List<DeliveryUnitDto> toDeliveryUnitDto(Collection<DeliveryUnit> deliveryUnits){
        return deliveryUnits.stream()
                .map(DeliveryUnitMapper::toDeliveryUnitDto)
                .collect(Collectors.toList());
    }
}
