package org.wms.dto.quote;

import org.wms.model.auth.User;
import org.wms.model.client.Client;
import org.wms.model.quote.Quote;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class QuoteDto {
    private Integer idQuote;
    private LocalDateTime quoteDate;
    private BigDecimal total;
    private String status;
    private Integer idClient;
    private Long idUser;
    private List<QuoteDetailDto> details;

    public QuoteDto() {
    }

    public QuoteDto(Quote quote) {
        if (quote == null) {
            return;
        }

        this.idQuote = quote.getIdQuote();
        this.quoteDate = quote.getQuoteDate();
        this.total = quote.getTotal();
        this.status = quote.getStatus();
        this.idClient = quote.getClient() != null ? quote.getClient().getIdClient() : null;
        this.idUser = quote.getUser() != null ? quote.getUser().getId() : null;

        if (quote.getDetails() != null) {
            this.details = quote.getDetails().stream()
                    .map(detail -> {
                        QuoteDetailDto dto = new QuoteDetailDto();
                        dto.setIdProduct(detail.getProduct().getIdProduct());
                        dto.setQuantity(detail.getQuantity());
                        dto.setUnitPrice(detail.getUnitPrice());
                        dto.setDiscount(detail.getDiscount());
                        return dto;
                    })
                    .toList();
        }
    }

    // Getters y setters

    public Integer getIdQuote() {
        return idQuote;
    }

    public void setIdQuote(Integer idQuote) {
        this.idQuote = idQuote;
    }

    public LocalDateTime getQuoteDate() {
        return quoteDate;
    }

    public void setQuoteDate(LocalDateTime quoteDate) {
        this.quoteDate = quoteDate;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getIdClient() {
        return idClient;
    }

    public void setIdClient(Integer idClient) {
        this.idClient = idClient;
    }

    public Long getIdUser() {
        return idUser;
    }

    public void setIdUser(Long idUser) {
        this.idUser = idUser;
    }

    public List<QuoteDetailDto> getDetails() {
        return details;
    }

    public void setDetails(List<QuoteDetailDto> details) {
        this.details = details;
    }
}
