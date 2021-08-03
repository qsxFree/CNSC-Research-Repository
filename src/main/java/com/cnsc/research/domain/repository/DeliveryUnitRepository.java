package com.cnsc.research.domain.repository;

import com.cnsc.research.domain.model.DeliveryUnit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeliveryUnitRepository extends JpaRepository<DeliveryUnit,Integer> {
}
