package org.wms.model.client;

import jakarta.persistence.*;
import org.wms.model.auth.User;

import java.time.LocalDateTime;

@Entity
@Table(name = "actividadcliente")
public class ClientActivity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idActividad")
    private Integer idActivity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idCliente", nullable = false)
    private Client client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idUsuario", nullable = false)
    private User user;

    @Column(name = "tipoActividad", nullable = false, length = 50)
    private String activityType;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String description;

    @Column(name = "fechaActividad", nullable = false)
    private LocalDateTime activityDate;

    @Column(name = "createdAt", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updatedAt", nullable = false)
    private LocalDateTime updatedAt;

    // --- Constructors ---
    public ClientActivity() {}

    public ClientActivity(Client client, User user, String activityType, String description,
                          LocalDateTime activityDate, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.client = client;
        this.user = user;
        this.activityType = activityType;
        this.description = description;
        this.activityDate = activityDate;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // --- Getters & Setters ---
    public Integer getIdActivity() {
        return idActivity;
    }

    public void setIdActivity(Integer idActivity) {
        this.idActivity = idActivity;
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

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getActivityDate() {
        return activityDate;
    }

    public void setActivityDate(LocalDateTime activityDate) {
        this.activityDate = activityDate;
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