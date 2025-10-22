package org.wms.dto.campaign;

import org.wms.model.campaign.CampaignClient;

import java.time.LocalDateTime;

public class CampaignClientDto {

    private Integer idCampaignClient;
    private Integer campaignId;
    private Integer clientId;
    private String result;
    private LocalDateTime interactionDate;
    private String comments;

    public CampaignClientDto() {
    }

    // Constructor desde la entidad
    public CampaignClientDto(CampaignClient campaignClient) {
        if (campaignClient == null) return;
        this.idCampaignClient = campaignClient.getIdCampaignClient();
        this.campaignId = campaignClient.getCampaign() != null ? campaignClient.getCampaign().getIdCampaign() : null;
        this.clientId = campaignClient.getClient() != null ? campaignClient.getClient().getIdClient() : null;
        this.result = campaignClient.getResult();
        this.interactionDate = campaignClient.getInteractionDate();
        this.comments = campaignClient.getComments();
    }

    // --- Getters y Setters ---
    public Integer getIdCampaignClient() {
        return idCampaignClient;
    }

    public void setIdCampaignClient(Integer idCampaignClient) {
        this.idCampaignClient = idCampaignClient;
    }

    public Integer getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(Integer campaignId) {
        this.campaignId = campaignId;
    }

    public Integer getClientId() {
        return clientId;
    }

    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public LocalDateTime getInteractionDate() {
        return interactionDate;
    }

    public void setInteractionDate(LocalDateTime interactionDate) {
        this.interactionDate = interactionDate;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}
