package org.wms.dto.sale;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.wms.model.sale.Sale;

public class SaleDto {
    private Long idSale;
    private LocalDateTime saleDate;
    private BigDecimal total;
    private Boolean status;
    private Integer idClient;
    private List<SaleDetailDto> details;

    public SaleDto() {
    }

    public SaleDto(Sale sale) {
        if (sale == null) {
            return;
        }

        this.idSale = sale.getIdSale();
        this.saleDate = sale.getSaleDate();
        this.total = sale.getTotal();
        this.status = sale.getStatus();
        this.idClient = sale.getClient() != null ? sale.getClient().getIdClient() : null;

        if (sale.getDetails() != null) {
            this.details = sale.getDetails().stream()
                    .map(detail -> {
                        SaleDetailDto dto = new SaleDetailDto();
                        dto.setIdProduct(detail.getProduct().getIdProduct());
                        dto.setAmount(detail.getAmount());
                        dto.setUnitPrice(detail.getUnitPrice());
                        dto.setDiscount(detail.getDiscount());
                        return dto;
                    })
                    .toList();
        }
    }


    // Getters y setters

    public Long getIdSale() {
        return idSale;
    }

    public void setIdSale(Long idSale) {
        this.idSale = idSale;
    }

    public LocalDateTime getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(LocalDateTime saleDate) {
        this.saleDate = saleDate;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Integer getIdClient() {
        return idClient;
    }

    public void setIdClient(Integer idClient) {
        this.idClient = idClient;
    }

    public List<SaleDetailDto> getDetails() {
        return details;
    }

    public void setDetails(List<SaleDetailDto> details) {
        this.details = details;
    }
}
