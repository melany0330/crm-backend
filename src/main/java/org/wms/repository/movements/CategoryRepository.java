package org.wms.repository.movements;

import org.springframework.data.jpa.repository.JpaRepository;
import org.wms.model.movements.Category;

public interface CategoryRepository extends JpaRepository <Category, Integer> {
}
