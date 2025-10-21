package org.wms.dto.quote;

import org.wms.model.quote.QuoteDetail;
import org.wms.model.movements.Product;

import java.math.BigDecimal;

public class QuoteDetailDto {
    private Integer idProduct;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal discount;

    public QuoteDetailDto() {}

    public QuoteDetailDto(QuoteDetail detail) {
        if (detail == null) {
            return;
        }

        this.idProduct = detail.getProduct() != null ? detail.getProduct().getIdProduct() : null;
        this.quantity = detail.getQuantity();
        this.unitPrice = detail.getUnitPrice();
        this.discount = detail.getDiscount();
    }

    // Getters y setters

    public Integer getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(Integer idProduct) {
        this.idProduct = idProduct;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }
}