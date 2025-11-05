package org.wms.dto.sale;

import java.math.BigDecimal;
import org.wms.model.sale.SaleDetail;

public class SaleDetailDto {
    private Integer idProduct;      // id del producto
    private String  productName;    // nombre del producto
    private Integer amount;
    private BigDecimal unitPrice;
    private BigDecimal discount;
    private BigDecimal subtotal;    // opcional: total de la línea

    public SaleDetailDto() {}

    public SaleDetailDto(SaleDetail detail) {
        if (detail == null) return;

        if (detail.getProduct() != null) {
            // Ajusta los getters si tu entidad Product usa otros nombres
            this.idProduct   = detail.getProduct().getIdProduct();
            this.productName = detail.getProduct().getName();
        }

        this.amount    = detail.getAmount();
        this.unitPrice = detail.getUnitPrice();
        this.discount  = detail.getDiscount();

        // subtotal = unitPrice * amount - discount (si hay)
        BigDecimal qty = BigDecimal.valueOf(this.amount == null ? 0 : this.amount);
        BigDecimal price = this.unitPrice == null ? BigDecimal.ZERO : this.unitPrice;
        BigDecimal disc  = this.discount  == null ? BigDecimal.ZERO : this.discount;
        this.subtotal = price.multiply(qty).subtract(disc);
    }

    // Getters y setters
    public Integer getIdProduct() { return idProduct; }
    public void setIdProduct(Integer idProduct) { this.idProduct = idProduct; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public Integer getAmount() { return amount; }
    public void setAmount(Integer amount) { this.amount = amount; }

    public BigDecimal getUnitPrice() { return unitPrice; }
    public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }

    public BigDecimal getDiscount() { return discount; }
    public void setDiscount(BigDecimal discount) { this.discount = discount; }

    public BigDecimal getSubtotal() { return subtotal; }
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }
}
