package org.wms.repository.purchases;

import org.springframework.data.jpa.repository.JpaRepository;
import org.wms.model.purchases.Purchase;
import org.wms.model.purchases.PurchaseBill;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface PurchaseRepository extends JpaRepository<Purchase, Integer> {
}
