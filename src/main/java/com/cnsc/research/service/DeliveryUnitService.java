package com.cnsc.research.service;

import com.cnsc.research.domain.mapper.DeliveryUnitMapper;
import com.cnsc.research.domain.mapper.ResearchMapper;
import com.cnsc.research.domain.model.DeliveryUnit;
import com.cnsc.research.domain.model.Research;
import com.cnsc.research.domain.repository.DeliveryUnitRepository;
import com.cnsc.research.domain.repository.ResearchRepository;
import com.cnsc.research.domain.transaction.DeliveryUnitDto;
import com.cnsc.research.domain.transaction.ResearchDto;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static java.lang.String.format;
import static org.springframework.http.HttpStatus.*;

@Service
public class DeliveryUnitService {
    public final DeliveryUnitRepository deliveryUnitRepository;
    public final ResearchRepository researchRepository;
    public final Logger logger;
    public final DeliveryUnitMapper mapper;
    public final ResearchMapper researchMapper;

    @Autowired
    public DeliveryUnitService(DeliveryUnitRepository deliveryUnitRepository, ResearchRepository researchRepository, Logger logger, DeliveryUnitMapper mapper, ResearchMapper researchMapper) {
        this.deliveryUnitRepository = deliveryUnitRepository;
        this.researchRepository = researchRepository;
        this.logger = logger;
        this.mapper = mapper;
        this.researchMapper = researchMapper;
    }

    public ResponseEntity getDistinctDeliveryUnitName() {
        try {
            return new ResponseEntity<List<DeliveryUnitDto>>(mapper.toTransaction(deliveryUnitRepository.findDistinctByOrderByUnitNameAsc()), OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("Error on retrieving Delivery Unit", INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity addDeliveryUnit(DeliveryUnitDto deliveryUnitDto) {
        Optional<DeliveryUnit> deliveryUnitOptional = deliveryUnitRepository
                .findByUnitNameIgnoreCase(deliveryUnitDto.getUnitName());

        if (deliveryUnitOptional.isEmpty()) {
            try {
                deliveryUnitRepository.save(mapper.toDomain(deliveryUnitDto));
                return new ResponseEntity<String>(format("%s has successfully added", deliveryUnitDto.getUnitName()), CREATED);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error(e.getMessage());
                return new ResponseEntity<String>(format("error on saving %s", deliveryUnitDto.getUnitName()), INTERNAL_SERVER_ERROR);
            }
        } else {
            return new ResponseEntity<String>(format("%s already exist", deliveryUnitDto.getUnitName()), BAD_REQUEST);
        }
    }

    public ResponseEntity editDeliveryUnit(DeliveryUnitDto deliveryUnitDto) {
        Optional<DeliveryUnit> deliveryUnitOptional = deliveryUnitRepository
                .findByUnitNameIgnoreCase(deliveryUnitDto.getUnitName());

        if (deliveryUnitOptional.isEmpty()) {
            try {
                deliveryUnitRepository.save(mapper.toDomain(deliveryUnitDto));
                return new ResponseEntity<String>(format("%s has successfully edited", deliveryUnitDto.getUnitName()), OK);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error(e.getMessage());
                return new ResponseEntity<String>(format("error on editing %s", deliveryUnitDto.getUnitName()), INTERNAL_SERVER_ERROR);
            }
        } else {
            return new ResponseEntity<String>(format("%s already exist", deliveryUnitDto.getUnitName()), BAD_REQUEST);
        }
    }

    public ResponseEntity deleteDeliveryUnit(Integer id) {
        try {
            long referenceCount = referenceCount(id);
            if (referenceCount <= 0) {
                deliveryUnitRepository.deleteById(id);
                return new ResponseEntity<String>("Delete success", OK);
            } else {
                return new ResponseEntity<String>("The delivery unit is referenced to " + referenceCount + " researches.", BAD_REQUEST);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            return new ResponseEntity<String>("Error on deleting delivery unit", INTERNAL_SERVER_ERROR);
        }
    }

    public long referenceCount(Integer id) {
        return researchRepository.countByDeliveryUnit_UnitId(id);
    }

    public ResponseEntity getDeliveryUnitById(Integer id) {
        try {
            Optional<DeliveryUnit> deliveryUnitOptional = deliveryUnitRepository.findById(id);
            if (deliveryUnitOptional.isPresent()) {
                return new ResponseEntity<DeliveryUnitDto>(mapper.toTransaction(deliveryUnitOptional.get()), OK);
            } else {
                return new ResponseEntity<String>("Can't retrieve delivery unit", BAD_REQUEST);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            return new ResponseEntity<String>("Can't retrieve delivery unit", INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity getResearches(Integer id) {
        try {
            List<Research> research = researchRepository.findByDeliveryUnit_UnitIdAndDeletedIsFalse(id);
            List<ResearchDto> researchersDtos = researchMapper.toTransaction(research);
            return new ResponseEntity<List<ResearchDto>>(researchersDtos, OK);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            return new ResponseEntity<String>("Can't retrieve researches", INTERNAL_SERVER_ERROR);
        }
    }
}
