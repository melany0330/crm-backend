package org.wms.dto.client;

import org.wms.model.client.ClientActivity;

import java.time.LocalDateTime;

public class ClientActivityDto {

    private Integer idActivity;
    private Integer clientId;
    private Integer userId;
    private String activityType;
    private String description;
    private LocalDateTime activityDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // --- Constructors ---
    public ClientActivityDto() {}

    public ClientActivityDto(ClientActivity activity) {
        this.idActivity = activity.getIdActivity();
        this.clientId = activity.getClient().getIdClient();
        this.userId = activity.getUser().getId().intValue();
        this.activityType = activity.getActivityType();
        this.description = activity.getDescription();
        this.activityDate = activity.getActivityDate();
        this.createdAt = activity.getCreatedAt();
        this.updatedAt = activity.getUpdatedAt();
    }

    // --- Getters & Setters ---
    public Integer getIdActivity() {
        return idActivity;
    }

    public void setIdActivity(Integer idActivity) {
        this.idActivity = idActivity;
    }

    public Integer getClientId() {
        return clientId;
    }

    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
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
