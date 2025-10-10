package org.wms.repository.purchases;

import org.springframework.data.jpa.repository.JpaRepository;
import org.wms.model.purchases.PurchaseDetail;

public interface PurchaseDetailRepository extends JpaRepository<PurchaseDetail, Integer> {
}
