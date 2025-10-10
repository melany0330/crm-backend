package org.wms.dto.purchases;

import org.wms.model.movements.Product;

import java.math.BigDecimal;

public class PurchaseDetailDto {
    private Product product;
    private Integer amount;
    private BigDecimal unitPrice;

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }
}
