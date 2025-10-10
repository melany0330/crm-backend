package org.wms.controller.movements;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.wms.dto.movements.ProductDto;
import org.wms.dto.movements.ProductRequestDto;
import org.wms.model.movements.Product;
import org.wms.service.movements.ProductService;
import org.wms.util.ApiResponse;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
@Tag(name = "Products", description = "Routes for product management")
public class ProductController {
    @Autowired
    private ProductService productService;

    @Operation(summary = "Get all products")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Products found",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ProductRequestDto.class))
    )

    @GetMapping("/list")
    public ResponseEntity<?> listAll(){
        List<ProductDto> list = productService.listAll();
        return ResponseEntity.ok(new ApiResponse<>("Products found", list));
    }

    @Operation(summary = "Get product catalog")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Product catalog found",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ProductRequestDto.class))
    )

    @GetMapping("/catalog")
    public ResponseEntity<?> catalog(){
        List<ProductDto> list = productService.catalog();
        return ResponseEntity.ok(new ApiResponse<>("Product catalog found", list));
    }

    @Operation(summary = "Get product by ID")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Product found",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ProductRequestDto.class))
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Product not found",
            content = @Content
    )

    @GetMapping("/listById/{idProduct}")
    public ResponseEntity<?> listById(@PathVariable Integer idProduct){
        Optional<ProductDto> product = productService.listById(idProduct);

        if(product.isPresent()){
            return ResponseEntity.ok(new ApiResponse<>("Product found",product));
        }
        else{
            return ResponseEntity.status(404).body(new ApiResponse<>("Product not found"));
        }
    }


    @Operation(summary = "Create a new Product")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Product created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProductRequestDto.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Failed to create product", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Category not found", content = @Content)
    })
    @PostMapping("/create")
    public ResponseEntity<?> createWithImage(
            @RequestPart("product") ProductRequestDto dto,
            @RequestPart("image") MultipartFile imageFile
    ) {
        try {
            Product product = productService.create(dto, imageFile);
            return ResponseEntity.ok(new ApiResponse<>("Product created successfully", new ProductRequestDto(product)));
        } catch (Exception e) {
            e.printStackTrace();
            if(e.getMessage().equals("Category not found")){
                return ResponseEntity.status(404).body(
                        new ApiResponse<>("Category not found")
                );
            }
            return ResponseEntity.badRequest().body(
                    new ApiResponse<>("Failed to create product"));

        }
    }

    @Operation(summary = "Update product by ID")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Product updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProductRequestDto.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Failed to update product", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Product or Category not found", content = @Content)
    })

    @PutMapping("/update/{idProduct}")
    public ResponseEntity<?> updateWithImage(
            @PathVariable Integer idProduct,
            @RequestPart("product") ProductRequestDto dto,
            @RequestPart(value = "image", required = false) MultipartFile imageFile
    ) {
        try {
            Product updatedProduct = productService.update(idProduct, dto, imageFile);
            ProductRequestDto responseDto = new ProductRequestDto(updatedProduct);
            return ResponseEntity.ok(new ApiResponse<>("Product updated successfully", responseDto));
        } catch (RuntimeException e) {
            if (e.getMessage().equals("Product not found")) {
                return ResponseEntity.status(404).body(new ApiResponse<>("Product not found"));
            }
            if (e.getMessage().equals("Category not found")) {
                return ResponseEntity.status(404).body(new ApiResponse<>("Category not found"));
            }
            return ResponseEntity.badRequest().body(
                    new ApiResponse<>("Failed to update Product"));
        }
    }


    @Operation(summary = "Deactivate a product by ID")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Product deactivated successfully", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Product not found", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Failed to deactivate product", content = @Content)
    })

    @PatchMapping("/deactivate/{idProduct}")
    public ResponseEntity<?> deactivate(@PathVariable Integer idProduct) {
        try {
            productService.deactivate(idProduct);
            return ResponseEntity.ok(new ApiResponse<>("Product deactivated successfully"));
        } catch (RuntimeException e) {
            if (e.getMessage().equals("Product not found")) {
                return ResponseEntity.status(404).body(
                        new ApiResponse<>("Product not found")
                );
            }
            return ResponseEntity.badRequest().body(
                    new ApiResponse<>("Failed to deactivate product")
            );
        }
    }

    @Operation(summary = "Activate a product by ID")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Product activated successfully", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Product not found", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Failed to activate product", content = @Content)
    })

    @PatchMapping("/activate/{idProduct}")
    public ResponseEntity<?> activate(@PathVariable Integer idProduct) {
        try {
            productService.activate(idProduct);
            return ResponseEntity.ok(new ApiResponse<>("Product activated successfully"));
        } catch (RuntimeException e) {
            if (e.getMessage().equals("Product not found")) {
                return ResponseEntity.status(404).body(
                        new ApiResponse<>("Product not found")
                );
            }
            return ResponseEntity.badRequest().body(
                    new ApiResponse<>("Failed to activate product")
            );
        }
    }
}
