package org.wms.dto.opportunity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class OpportunityDto {

    private Integer idOpportunity;

    @NotNull(message = "Client ID must not be null")
    private Integer clientId;

    private Integer quoteId; // optional

    @NotNull(message = "User ID must not be null")
    private Long userId;

    @NotBlank(message = "Status must not be blank")
    private String status;

    private BigDecimal probability;
    private BigDecimal estimatedValue;
    private LocalDateTime expectedCloseDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public OpportunityDto() {
    }

    // Constructor desde la entidad
    public OpportunityDto(org.wms.model.opportunity.Opportunity opportunity) {
        if (opportunity == null) return;

        this.idOpportunity = opportunity.getIdOpportunity();
        this.clientId = opportunity.getClient() != null ? opportunity.getClient().getIdClient() : null;
        this.quoteId = opportunity.getQuote() != null ? opportunity.getQuote().getIdQuote() : null;
        this.userId = opportunity.getUser() != null ? opportunity.getUser().getId() : null;
        this.status = opportunity.getStatus();
        this.probability = opportunity.getProbability();
        this.estimatedValue = opportunity.getEstimatedValue();
        this.expectedCloseDate = opportunity.getExpectedCloseDate();
        this.createdAt = opportunity.getCreatedAt();
        this.updatedAt = opportunity.getUpdatedAt();
    }

    // Getters y setters
    public Integer getIdOpportunity() {
        return idOpportunity;
    }

    public void setIdOpportunity(Integer idOpportunity) {
        this.idOpportunity = idOpportunity;
    }

    public Integer getClientId() {
        return clientId;
    }

    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    public Integer getQuoteId() {
        return quoteId;
    }

    public void setQuoteId(Integer quoteId) {
        this.quoteId = quoteId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getProbability() {
        return probability;
    }

    public void setProbability(BigDecimal probability) {
        this.probability = probability;
    }

    public BigDecimal getEstimatedValue() {
        return estimatedValue;
    }

    public void setEstimatedValue(BigDecimal estimatedValue) {
        this.estimatedValue = estimatedValue;
    }

    public LocalDateTime getExpectedCloseDate() {
        return expectedCloseDate;
    }

    public void setExpectedCloseDate(LocalDateTime expectedCloseDate) {
        this.expectedCloseDate = expectedCloseDate;
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
}
