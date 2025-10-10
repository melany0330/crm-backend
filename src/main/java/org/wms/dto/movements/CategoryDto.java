package org.wms.dto.movements;

import org.wms.model.movements.Category;

public class CategoryDto {
    private Integer idCategory;
    private String categoryName;
    public CategoryDto() {

    }

    public CategoryDto(Category category) {
        this.idCategory = category.getIdCategory();
        this.categoryName = category.getCategoryName();
    }

    public Integer getIdCategory() {
        return idCategory;
    }

    public void setIdCategory(Integer idCategory) {
        this.idCategory = idCategory;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }


}
