package org.wms.dto.purchases;

import org.wms.model.purchases.PurchaseBill;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PurchaseBillDto {
    private Integer idPurchaseBill;
    private String series;
    private String billNumber;
    private LocalDateTime issueDate;
    private BigDecimal billTotal;
    private PurchaseDto purchase;


    public PurchaseBillDto(PurchaseBill purchaseBill) {
        this.idPurchaseBill = purchaseBill.getIdPurchaseBill();
        this.series = purchaseBill.getSeries();
        this.billNumber = purchaseBill.getBillNumber();
        this.issueDate = purchaseBill.getIssueDate();
        this.billTotal = purchaseBill.getBillTotal();
        this.purchase = new PurchaseDto(purchaseBill.getPurchase());
    }

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

    public PurchaseDto getPurchase() {
        return purchase;
    }

    public void setPurchase(PurchaseDto purchase) {
        this.purchase = purchase;
    }
}
