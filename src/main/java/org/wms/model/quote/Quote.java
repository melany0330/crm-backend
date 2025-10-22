package org.wms.model.quote;

import jakarta.persistence.*;
import org.wms.model.client.Client;
import org.wms.model.auth.User;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "Cotizacion")
public class Quote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idCotizacion")
    private Integer idQuote;

    @ManyToOne
    @JoinColumn(name = "idCliente", nullable = false)
    private Client client;

    @ManyToOne(optional = true)
    @JoinColumn(name = "idUsuario", nullable = true)
    private User user;

    @Column(name = "fechaCotizacion", nullable = false)
    private LocalDateTime quoteDate;

    @Column(name = "total", nullable = false, precision = 10, scale = 2)
    private BigDecimal total;

    @Column(name = "estado", length = 50, nullable = false)
    private String status; // Pendiente, Aceptada, Rechazada

    @Column(name = "createdAt", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updatedAt", nullable = false)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "quote", cascade = CascadeType.ALL)
    private List<QuoteDetail> details;

    // Getters y Setters
    public Integer getIdQuote() {
        return idQuote;
    }

    public void setIdQuote(Integer idQuote) {
        this.idQuote = idQuote;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

    public List<QuoteDetail> getDetails() {
        return details;
    }

    public void setDetails(List<QuoteDetail> details) {
        this.details = details;
    }
}
