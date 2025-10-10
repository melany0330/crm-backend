package org.wms.repository.sale;

import org.springframework.data.jpa.repository.JpaRepository;
import org.wms.model.sale.SaleDetail;

public interface SaleDetailRepository extends JpaRepository<SaleDetail, Integer> {
}
