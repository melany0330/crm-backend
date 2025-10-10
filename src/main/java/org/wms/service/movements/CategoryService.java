package org.wms.service.movements;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wms.dto.movements.CategoryDto;
import org.wms.model.movements.Category;
import org.wms.repository.movements.CategoryRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    @Autowired
    CategoryRepository categoryRepository;
    public List<CategoryDto> listAll() {
        return categoryRepository.findAll()
                .stream()
                .filter(Category::getStatus)
                .map(CategoryDto::new)
                .toList();
    }

    public Optional<CategoryDto> listById(Integer idCategory) {
        return categoryRepository.findById(idCategory).map(CategoryDto::new);
    }

    public Category create(CategoryDto dto) {
        Category category = new Category();
        category.setCategoryName(dto.getCategoryName());
        category.setStatus(true);
        category.setCreatedAt(LocalDateTime.now());
        category.setUpdatedAt(LocalDateTime.now());
        return categoryRepository.save(category);
    }

    public Category update(Integer idCategory, CategoryDto dto) {
        Category category = categoryRepository.findById(idCategory)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        category.setCategoryName(dto.getCategoryName());
        category.setUpdatedAt(LocalDateTime.now());
        return categoryRepository.save(category);
    }

    public void deactivate(Integer idCategory) {
        Category category = categoryRepository.findById(idCategory)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        category.setStatus(false);
        category.setUpdatedAt(LocalDateTime.now());
        categoryRepository.save(category);
    }
}
