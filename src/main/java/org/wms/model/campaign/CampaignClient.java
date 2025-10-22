package org.wms.model.campaign;

import jakarta.persistence.*;
import org.wms.model.client.Client;

import java.time.LocalDateTime;

@Entity
@Table(name = "campaniacliente")
public class CampaignClient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idCampaniaCliente")
    private Integer idCampaignClient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idCampania", nullable = false)
    private Campaign campaign;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idCliente", nullable = false)
    private Client client;

    @Column(name = "resultado", length = 100)
    private String result;

    @Column(name = "fechaInteraccion")
    private LocalDateTime interactionDate;

    @Column(name = "comentarios", columnDefinition = "TEXT")
    private String comments;

    // --- Constructors ---
    public CampaignClient() {
    }

    public CampaignClient(Campaign campaign, Client client, String result, LocalDateTime interactionDate, String comments) {
        this.campaign = campaign;
        this.client = client;
        this.result = result;
        this.interactionDate = interactionDate;
        this.comments = comments;
    }

    // --- Getters & Setters ---
    public Integer getIdCampaignClient() {
        return idCampaignClient;
    }

    public void setIdCampaignClient(Integer idCampaignClient) {
        this.idCampaignClient = idCampaignClient;
    }

    public Campaign getCampaign() {
        return campaign;
    }

    public void setCampaign(Campaign campaign) {
        this.campaign = campaign;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
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