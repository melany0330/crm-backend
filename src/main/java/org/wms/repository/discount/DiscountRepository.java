package org.wms.repository.discount;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.wms.model.discount.Discount;
import org.wms.model.movements.Product;

import java.util.Optional;

public interface DiscountRepository extends JpaRepository<Discount, Integer> {

    @Query("SELECT d FROM Discount d WHERE d.product = :product AND d.estado = true AND :amount >= d.cantidadMin ORDER BY d.cantidadMin DESC")
    Optional<Discount> findBestActiveDiscount(@Param("product") Product product, @Param("amount") Integer amount);
}
