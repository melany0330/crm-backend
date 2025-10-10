package org.wms.controller.movements;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.wms.dto.movements.CategoryDto;
import org.wms.model.movements.Category;
import org.wms.service.movements.CategoryService;
import org.wms.util.ApiResponse;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/categories")
@Tag(name = "Categories", description = "Routes for category management")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Operation(summary = "Get all categories")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Categories found",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CategoryDto.class)))

    @GetMapping("/list")
    public ResponseEntity<?> listAll() {
        List<CategoryDto> list = categoryService.listAll();
        return ResponseEntity.ok(new ApiResponse<>("Categories found", list));
    }

    @Operation(summary = "Get category by ID")
    @io.swagger.v3.oas.annotations.responses.ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Category found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CategoryDto.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Category not found", content = @Content)
    })

    @GetMapping("/listById/{idCategory}")
    public ResponseEntity<?> listById(@PathVariable Integer idCategory) {
        Optional<CategoryDto> category = categoryService.listById(idCategory);

        if (category.isPresent()) {
            return ResponseEntity.ok(new ApiResponse<>("Category found", category.get()));
        } else {
            return ResponseEntity.status(404).body(new ApiResponse<>("Category not found"));
        }
    }

    @Operation(summary = "Create a new category")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Category created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CategoryDto.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Failed to create category", content = @Content)
    })

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody CategoryDto dto) {
        try {
            Category newCategory = categoryService.create(dto);
            CategoryDto responseDto = new CategoryDto(newCategory);
            return ResponseEntity.ok(new ApiResponse<>("Category created successfully", responseDto));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(
                    new ApiResponse<>("Failed to create category")
            );
        }
    }

    @Operation(summary = "Update a category by ID")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Category updated",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CategoryDto.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Category not found", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Failed to update category", content = @Content)
    })

    @PutMapping("/update/{idCategory}")
    public ResponseEntity<?> update(@PathVariable Integer idCategory, @RequestBody CategoryDto dto) {
        try {
            Category updated = categoryService.update(idCategory, dto);
            CategoryDto responseDto = new CategoryDto(updated);
            return ResponseEntity.ok(new ApiResponse<>("Category updated successfully", responseDto));
        } catch (RuntimeException e) {
            if ("Category not found".equals(e.getMessage())) {
                return ResponseEntity.status(404).body(
                        new ApiResponse<>("Category not found")
                );
            }
            return ResponseEntity.badRequest().body(
                    new ApiResponse<>("Failed to update category")
            );
        }
    }

    @Operation(summary = "Deactivate a category by ID")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Category deactivated successfully", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Category not found", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Failed to deactivate category", content = @Content)
    })

    @PutMapping("/deactivate/{idCategory}")
    public ResponseEntity<?> deactivate(@PathVariable Integer idCategory) {
        try {
            categoryService.deactivate(idCategory);
            return ResponseEntity.ok(new ApiResponse<>("Category deactivated successfully"));
        } catch (RuntimeException e) {
            if ("Category not found".equals(e.getMessage())) {
                return ResponseEntity.status(404).body(
                        new ApiResponse<>("Category not found")
                );
            }
            return ResponseEntity.badRequest().body(
                    new ApiResponse<>("Failed to deactivate category")
            );
        }
    }
}
