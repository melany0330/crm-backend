package org.wms.model.opportunity;

import jakarta.persistence.*;
import org.wms.model.client.Client;
import org.wms.model.quote.Quote;
import org.wms.model.auth.User;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "oportunidadventa")
public class Opportunity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idOportunidad")
    private Integer idOpportunity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idCliente", nullable = false)
    private Client client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idCotizacion")
    private Quote quote; // optional

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idUsuario", nullable = false)
    private User user;

    @Column(name = "estado", nullable = false)
    private String status;

    @Column(name = "probabilidad", nullable = false, precision = 5, scale = 2)
    private BigDecimal probability;

    @Column(name = "valorEstimado", precision = 10, scale = 2)
    private BigDecimal estimatedValue;

    @Column(name = "fechaCierreEsperada")
    private LocalDateTime expectedCloseDate;

    @Column(name = "createdAt", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updatedAt", nullable = false)
    private LocalDateTime updatedAt;

    // Getters y setters
    public Integer getIdOpportunity() {
        return idOpportunity;
    }

    public void setIdOpportunity(Integer idOpportunity) {
        this.idOpportunity = idOpportunity;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Quote getQuote() {
        return quote;
    }

    public void setQuote(Quote quote) {
        this.quote = quote;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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
