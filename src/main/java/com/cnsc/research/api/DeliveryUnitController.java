package com.cnsc.research.api;

import com.cnsc.research.domain.transaction.DeliveryUnitDto;
import com.cnsc.research.service.DeliveryUnitService;
import org.slf4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/delivery-unit")
public class DeliveryUnitController {
    private final DeliveryUnitService service;
    private final Logger logger;

    public DeliveryUnitController(DeliveryUnitService service, Logger logger) {
        this.logger = logger;
        this.service = service;
    }

    @GetMapping("/list")
    public List<DeliveryUnitDto> getDeliveryUnits() {
        return service.getDistinctDeliveryUnitName();
    }

    @PostMapping
    public ResponseEntity addDeliveryUnit(@RequestBody DeliveryUnitDto deliveryUnitDto) {
        return service.addDeliveryUnit(deliveryUnitDto);
    }

    @PutMapping
    public ResponseEntity editDeliveryUnit(@RequestBody DeliveryUnitDto deliveryUnitDto) {
        return service.editDeliveryUnit(deliveryUnitDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteDeliveryUnit(@PathVariable(name = "id") Integer id) {
        return service.deleteDeliveryUnit(id);
    }

    @GetMapping("/{id}")
    public ResponseEntity getDeliveryUnit(@PathVariable(name = "id") Integer id) {
        return service.getDeliveryUnitById(id);
    }

    @GetMapping("/{id}/research/list")
    public ResponseEntity getResearches(@PathVariable(name = "id") Integer id) {
        return service.getResearches(id);
    }
}
