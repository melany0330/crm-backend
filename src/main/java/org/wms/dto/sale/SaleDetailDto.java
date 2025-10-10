package org.wms.dto.sale;

import org.wms.model.sale.SaleDetail;
import java.math.BigDecimal;

public class SaleDetailDto {
    private Integer idProduct;
    private Integer amount;
    private BigDecimal unitPrice;
    private BigDecimal discount;


    public SaleDetailDto() {}


    public SaleDetailDto(SaleDetail detail) {
        this.idProduct = detail.getProduct() != null ? detail.getProduct().getIdProduct() : null;
        this.amount = detail.getAmount();
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

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }
}
