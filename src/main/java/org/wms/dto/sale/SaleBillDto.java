package org.wms.dto.sale;

import org.wms.model.sale.Sale;
import org.wms.model.sale.Invoice;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class SaleBillDto {


    private Long idSale;
    private LocalDateTime saleDate;
    private BigDecimal total;
    private Boolean status;
    private Integer idClient;


    private List<SaleDetailDto> details;


    private InvoiceDto invoice;


    public SaleBillDto() {}


    public SaleBillDto(Sale sale, Invoice invoice) {
        this.idSale = sale.getIdSale();
        this.saleDate = sale.getSaleDate();
        this.total = sale.getTotal();
        this.status = sale.getStatus();
        this.idClient = sale.getClient() != null ? sale.getClient().getIdClient() : null;


        this.details = sale.getDetails() != null ? sale.getDetails()
                .stream()
                .map(SaleDetailDto::new)
                .collect(Collectors.toList()) : null;


        this.invoice = invoice != null ? new InvoiceDto(invoice) : null;
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

    public InvoiceDto getInvoice() {
        return invoice;
    }

    public void setInvoice(InvoiceDto invoice) {
        this.invoice = invoice;
    }
}
