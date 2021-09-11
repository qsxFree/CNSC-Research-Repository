package com.cnsc.research.api;

import com.cnsc.research.domain.transaction.DeliveryUnitDto;
import com.cnsc.research.service.DeliveryUnitService;
import org.slf4j.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/delivery-unit")
public class DeliveryUnitController {
    private final DeliveryUnitService service;
    private final Logger logger;
    public DeliveryUnitController(DeliveryUnitService service,Logger logger){
        this.logger = logger;
        this.service = service;
    }


    @GetMapping("/list")
    public List<DeliveryUnitDto> getDistinctDeliveryUnitName(){
        return service.getDistinctDeliveryUnitName();
    }
}
