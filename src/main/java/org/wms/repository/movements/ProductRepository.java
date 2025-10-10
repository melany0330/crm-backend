package org.wms.repository.movements;

import org.springframework.data.jpa.repository.JpaRepository;
import org.wms.model.movements.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {
}
