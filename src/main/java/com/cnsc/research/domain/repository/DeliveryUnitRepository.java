package com.cnsc.research.domain.repository;

import com.cnsc.research.domain.model.DeliveryUnit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DeliveryUnitRepository extends JpaRepository<DeliveryUnit,Integer> {
    Optional<DeliveryUnit> findByUnitNameIgnoreCase(@Nullable String unitName);

}
