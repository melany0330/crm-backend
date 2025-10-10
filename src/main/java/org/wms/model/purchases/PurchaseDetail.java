package org.wms.model.purchases;

import jakarta.persistence.*;
import org.wms.model.movements.Product;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "DetalleCompra")
public class PurchaseDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="idDetalleCompra")
    private Integer idPurchaseDetail;

    @ManyToOne
    @JoinColumn(name = "idProducto", nullable = false)
    private Product product;

    @Column(name="cantidad",nullable = false)
    private Integer amount;

    @Column(name="precioUnitario",nullable = false)
    private BigDecimal unitPrice;

    @Column(name = "createdAt", nullable = false)
    private LocalDateTime createdAt;
    @Column(name = "updatedAt", nullable = false)
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name="idCompra",nullable = false)
    private Purchase purchase;

    public Integer getIdPurchaseDetail() {
        return idPurchaseDetail;
    }

    public void setIdPurchaseDetail(Integer idPurchaseDetail) {
        this.idPurchaseDetail = idPurchaseDetail;
    }

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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Purchase getPurchase() {
        return purchase;
    }

    public void setPurchase(Purchase purchase) {
        this.purchase = purchase;
    }
}
