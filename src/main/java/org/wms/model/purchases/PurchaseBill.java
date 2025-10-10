package org.wms.model.purchases;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table (name = "FacturaCompra")
public class PurchaseBill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idFacturaCompra")
    private Integer idPurchaseBill;

    @Column (name = "serieFactura", length = 20, nullable = false)
    private String series;

    @Column (name = "numeroFactura",length = 50, nullable = false)
    private String billNumber;

    @Column (name = "fechaEmision", nullable = false)
    private LocalDateTime issueDate;

    @Column(name = "totalFactura", nullable = false)
    private BigDecimal billTotal;

    @Column(name = "estado", nullable = false)
    private Boolean status;

    @Column(name = "createdAt", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updatedAt", nullable = false)
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "idCompra", nullable = false)
    private Purchase purchase;

    public Integer getIdPurchaseBill() {
        return idPurchaseBill;
    }

    public void setIdPurchaseBill(Integer idPurchaseBill) {
        this.idPurchaseBill = idPurchaseBill;
    }

    public String getSeries() {
        return series;
    }

    public void setSeries(String series) {
        this.series = series;
    }

    public String getBillNumber() {
        return billNumber;
    }

    public void setBillNumber(String billNumber) {
        this.billNumber = billNumber;
    }

    public LocalDateTime getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(LocalDateTime issueDate) {
        this.issueDate = issueDate;
    }

    public BigDecimal getBillTotal() {
        return billTotal;
    }

    public void setBillTotal(BigDecimal billTotal) {
        this.billTotal = billTotal;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
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
