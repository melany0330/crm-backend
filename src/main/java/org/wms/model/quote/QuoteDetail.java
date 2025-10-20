package org.wms.model.quote;

import jakarta.persistence.*;
import org.wms.model.movements.Product;

import java.math.BigDecimal;

@Entity
@Table(name = "DetalleCotizacion")
public class QuoteDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idDetalleCotizacion")
    private Integer idQuoteDetail;

    @ManyToOne
    @JoinColumn(name = "idCotizacion", nullable = false)
    private Quote quote;

    @ManyToOne
    @JoinColumn(name = "idProducto", nullable = false)
    private Product product;

    @Column(name = "cantidad", nullable = false)
    private Integer quantity;

    @Column(name = "precioUnitario", nullable = false, precision = 10, scale = 2)
    private BigDecimal unitPrice;

    @Column(name = "descuento", precision = 10, scale = 2)
    private BigDecimal discount;

    // Getters y Setters
    public Integer getIdQuoteDetail() {
        return idQuoteDetail;
    }

    public void setIdQuoteDetail(Integer idQuoteDetail) {
        this.idQuoteDetail = idQuoteDetail;
    }

    public Quote getQuote() {
        return quote;
    }

    public void setQuote(Quote quote) {
        this.quote = quote;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }
}
