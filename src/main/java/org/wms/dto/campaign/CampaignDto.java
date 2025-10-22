package org.wms.dto.campaign;

import org.wms.model.campaign.Campaign; // importa tu entity
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class CampaignDto {

    private Integer idCampaign;
    private String name;
    private String description;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private BigDecimal budget;
    private String type;
    private String objective;
    private String channel;
    private String status;
    private BigDecimal conversionRate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public CampaignDto() {
    }

    // Constructor desde la entidad
    public CampaignDto(Campaign campaign) {
        if (campaign == null) return;
        this.idCampaign = campaign.getIdCampaign();
        this.name = campaign.getCampaignName();
        this.description = campaign.getDescription();
        this.startDate = campaign.getStartDate();
        this.endDate = campaign.getEndDate();
        this.budget = campaign.getBudget();
        this.type = campaign.getCampaignType();
        this.objective = campaign.getObjective();
        this.channel = campaign.getChannel();
        this.status = campaign.getStatus();
        this.conversionRate = campaign.getConversionRate();
        this.createdAt = campaign.getCreatedAt();
        this.updatedAt = campaign.getUpdatedAt();
    }

    // --- Getters y Setters ---
    public Integer getIdCampaign() {
        return idCampaign;
    }

    public void setIdCampaign(Integer idCampaign) {
        this.idCampaign = idCampaign;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public BigDecimal getBudget() {
        return budget;
    }

    public void setBudget(BigDecimal budget) {
        this.budget = budget;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getObjective() {
        return objective;
    }

    public void setObjective(String objective) {
        this.objective = objective;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getConversionRate() {
        return conversionRate;
    }

    public void setConversionRate(BigDecimal conversionRate) {
        this.conversionRate = conversionRate;
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