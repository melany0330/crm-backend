package org.wms.repository.sale;

import org.springframework.data.jpa.repository.JpaRepository;
import org.wms.model.sale.Invoice;

public interface InvoiceRepository extends JpaRepository<Invoice, Integer> {

}
