package org.wms.dto.discount;

import org.wms.model.movements.Product;
import org.wms.model.discount.Discount;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class DiscountDto {
    private Integer idDescuento;
    private String nombreDescuento;
    private Integer cantidadMin;
    private BigDecimal porcentaje;
    private Boolean estado;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer idProducto;

    public DiscountDto() {
    }

    public DiscountDto(Discount discount) {
        this.idDescuento = discount.getIdDescuento();
        this.nombreDescuento = discount.getNombreDescuento();
        this.cantidadMin = discount.getCantidadMin();
        this.porcentaje = discount.getPorcentaje();
        this.estado = discount.getEstado();
        this.createdAt = discount.getCreatedAt();
        this.updatedAt = discount.getUpdatedAt();
        this.idProducto = discount.getProduct() != null ? discount.getProduct().getIdProduct() : null;
    }

    public Integer getIdDescuento() {
        return idDescuento;
    }

    public void setIdDescuento(Integer idDescuento) {
        this.idDescuento = idDescuento;
    }

    public String getNombreDescuento() {
        return nombreDescuento;
    }

    public void setNombreDescuento(String nombreDescuento) {
        this.nombreDescuento = nombreDescuento;
    }

    public Integer getCantidadMin() {
        return cantidadMin;
    }

    public void setCantidadMin(Integer cantidadMin) {
        this.cantidadMin = cantidadMin;
    }

    public BigDecimal getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(BigDecimal porcentaje) {
        this.porcentaje = porcentaje;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
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

    public Integer getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(Integer idProducto) {
        this.idProducto = idProducto;
    }
}
