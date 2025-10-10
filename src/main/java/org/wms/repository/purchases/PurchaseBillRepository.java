package org.wms.repository.purchases;

import org.springframework.data.jpa.repository.JpaRepository;
import org.wms.model.purchases.PurchaseBill;

import java.time.LocalDateTime;
import java.util.List;

public interface PurchaseBillRepository extends JpaRepository <PurchaseBill, Integer> {
    List<PurchaseBill> findByIssueDateBetween(LocalDateTime startDate, LocalDateTime endDate);
}
