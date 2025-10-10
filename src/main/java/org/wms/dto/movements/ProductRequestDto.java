package org.wms.dto.movements;

import org.wms.model.movements.Product;

import java.math.BigDecimal;

public class ProductRequestDto {
    private Integer idProduct;
    private String name;
    private String description;
    private String image;
    private BigDecimal salePrice;
    private BigDecimal purchasePrice;
    private Integer idCategory;

    public ProductRequestDto(){

    }
    public ProductRequestDto(Product product){
        this.idProduct = product.getIdProduct();
        this.name = product.getName();
        this.description = product.getDescription();
        this.image = product.getImage();
        this.salePrice = product.getSalePrice();
        this.purchasePrice = product.getPurchasePrice();
        this.idCategory = product.getCategory().getIdCategory();
    }

    public Integer getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(Integer idProduct) {
        this.idProduct = idProduct;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public BigDecimal getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(BigDecimal salePrice) {
        this.salePrice = salePrice;
    }

    public BigDecimal getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(BigDecimal purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public Integer getIdCategory() {
        return idCategory;
    }

    public void setIdCategory(Integer idCategory) {
        this.idCategory = idCategory;
    }
}
