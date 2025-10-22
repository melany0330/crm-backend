package org.wms.model.campaign;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "campania")
public class Campaign {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idCampania")
    private Integer idCampaign;

    @Column(name = "nombreCampania", nullable = false, length = 100)
    private String campaignName;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String description;

    @Column(name = "fechaInicio", nullable = false)
    private LocalDateTime startDate;

    @Column(name = "fechaFin", nullable = false)
    private LocalDateTime endDate;

    @Column(name = "presupuesto", precision = 10, scale = 2)
    private BigDecimal budget;

    @Column(name = "tipoCampania", length = 50)
    private String campaignType;

    @Column(name = "objetivo", length = 150)
    private String objective;

    @Column(name = "canal", length = 50)
    private String channel;

    @Column(name = "estado", nullable = false, length = 50)
    private String status;

    @Column(name = "tasaConversion", precision = 5, scale = 2)
    private BigDecimal conversionRate;

    @Column(name = "createdAt", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updatedAt", nullable = false)
    private LocalDateTime updatedAt;

    // --- Constructors ---
    public Campaign() {
    }

    public Campaign(String campaignName, String description, LocalDateTime startDate, LocalDateTime endDate,
                    BigDecimal budget, String campaignType, String objective, String channel,
                    String status, BigDecimal conversionRate, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.campaignName = campaignName;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.budget = budget;
        this.campaignType = campaignType;
        this.objective = objective;
        this.channel = channel;
        this.status = status;
        this.conversionRate = conversionRate;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // --- Getters & Setters ---

    public Integer getIdCampaign() {
        return idCampaign;
    }

    public void setIdCampaign(Integer idCampaign) {
        this.idCampaign = idCampaign;
    }

    public String getCampaignName() {
        return campaignName;
    }

    public void setCampaignName(String campaignName) {
        this.campaignName = campaignName;
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

    public String getCampaignType() {
        return campaignType;
    }

    public void setCampaignType(String campaignType) {
        this.campaignType = campaignType;
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