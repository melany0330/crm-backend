package org.wms.dto.purchases;
import org.wms.model.purchases.Provider;
import org.wms.model.purchases.Purchase;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class PurchaseDto {
    private Integer idPurchase;
    private LocalDate  purchaseDate;
    private BigDecimal totalAmount;
    private ProviderDto provider;
    private List<PurchaseDetailDto> details;

    public PurchaseDto() {

    }

    public PurchaseDto(Purchase purchase) {
        if (purchase == null) {
            return;
        }

        this.idPurchase = purchase.getIdPurchase();
        this.purchaseDate = purchase.getPurchaseDate();
        this.totalAmount = purchase.getTotalAmount();

        if (purchase.getProvider() != null) {
            this.provider = new ProviderDto(purchase.getProvider());
        }

        if (purchase.getDetails() != null) {
            this.details = purchase.getDetails().stream()
                    .map(detail -> {
                        PurchaseDetailDto dto = new PurchaseDetailDto();
                        dto.setProduct(detail.getProduct());
                        dto.setAmount(detail.getAmount());
                        dto.setUnitPrice(detail.getUnitPrice());
                        return dto;
                    })
                    .toList();
        }
    }

    public Integer getIdPurchase() {
        return idPurchase;
    }

    public void setIdPurchase(Integer idPurchase) {
        this.idPurchase = idPurchase;
    }

    public LocalDate getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDate purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public ProviderDto getProvider() {
        return provider;
    }

    public void setProvider(ProviderDto provider) {
        this.provider = provider;
    }

    public List<PurchaseDetailDto> getDetails() {
        return details;
    }

    public void setDetails(List<PurchaseDetailDto> details) {
        this.details = details;
    }
}
