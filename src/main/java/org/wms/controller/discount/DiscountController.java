package org.wms.controller.discount;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.wms.dto.discount.DiscountDto;
import org.wms.model.discount.Discount;
import org.wms.service.discount.DiscountService;
import org.wms.util.ApiResponse;

import java.util.List;
import java.util.Optional;

/**
 * Controller responsible for managing product discounts.
 * Includes operations to list, create, update, and deactivate discounts.
 */
@Tag(name = "Discounts", description = "Operations related to product discounts")
@RestController
@RequestMapping("/api/discounts")
public class DiscountController {

    @Autowired
    private DiscountService discountService;

    /**
     * Retrieve all active discounts.
     *
     * @return List of DiscountDto.
     */
    @Operation(
            summary = "Get all discounts",
            description = "Retrieve all active discounts"
    )
    @GetMapping("/list")
    public ResponseEntity<?> listAll() {
        List<DiscountDto> list = discountService.listAll();
        return ResponseEntity.ok(new ApiResponse<>("Discounts found", list));
    }

    /**
     * Retrieve all discounts(active and inactive).
     *
     * @return List of DiscountDto.
     */
    @Operation(
            summary = "Get all discounts",
            description = "Retrieve all discounts"
    )
    @GetMapping("/listAll")
    public ResponseEntity<?> listAllDiscounts() {
        List<DiscountDto> list = discountService.listAllDiscounts();
        return ResponseEntity.ok(new ApiResponse<>("Discounts found", list));
    }

    /**
     * Retrieve a discount by its ID.
     *
     * @param id Discount ID to fetch.
     * @return DiscountDto if found or 404 status if not.
     */
    @Operation(
            summary = "Get discount by ID",
            description = "Retrieve a discount by its ID"
    )
    @GetMapping("/listById/{id}")
    public ResponseEntity<?> listById(@PathVariable Integer id) {
        Optional<DiscountDto> discount = discountService.listById(id);
        if (discount.isPresent()) {
            return ResponseEntity.ok(new ApiResponse<>("Discount found", discount.get()));
        } else {
            return ResponseEntity.status(404).body(new ApiResponse<>("Discount not found"));
        }
    }

    /**
     * Create a new discount for a product.
     *
     * @param dto Discount data to create.
     * @return Created DiscountDto or error message.
     */
    @Operation(
            summary = "Create discount",
            description = "Create a new discount for a product"
    )
    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody DiscountDto dto) {
        try {
            Discount discount = discountService.create(dto);
            DiscountDto responseDto = new DiscountDto(discount);
            return ResponseEntity.ok(new ApiResponse<>("Discount created successfully", responseDto));
        } catch (RuntimeException e) {
            if ("Product not found".equals(e.getMessage())) {
                return ResponseEntity.status(404).body(new ApiResponse<>("Product not found"));
            }
            return ResponseEntity.badRequest().body(new ApiResponse<>("Failed to create discount"));
        }
    }

    /**
     * Update an existing discount by its ID.
     *
     * @param id  Discount ID to update.
     * @param dto Discount data for update.
     * @return Updated DiscountDto or error message.
     */
    @Operation(
            summary = "Update discount",
            description = "Update an existing discount by ID"
    )
    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody DiscountDto dto) {
        try {
            Discount updatedDiscount = discountService.update(id, dto);
            DiscountDto responseDto = new DiscountDto(updatedDiscount);
            return ResponseEntity.ok(new ApiResponse<>("Discount successfully updated", responseDto));
        } catch (RuntimeException e) {
            String msg = e.getMessage();
            if ("Discount not found".equals(msg)) {
                return ResponseEntity.status(404).body(new ApiResponse<>("Discount not found"));
            }
            if ("Product not found".equals(msg)) {
                return ResponseEntity.status(404).body(new ApiResponse<>("Product not found"));
            }
            return ResponseEntity.badRequest().body(new ApiResponse<>("Failed to update discount: " + msg));
        }
    }

    /**
     * Deactivate a discount by its ID.
     *
     * @param id Discount ID to deactivate.
     * @return Success message or error.
     */
    @Operation(
            summary = "Deactivate discount",
            description = "Deactivate a discount by ID"
    )
    @PutMapping("/deactivate/{id}")
    public ResponseEntity<?> deactivate(@PathVariable Integer id) {
        try {
            discountService.deactivate(id);
            return ResponseEntity.ok(new ApiResponse<>("Discount deactivated successfully"));
        } catch (RuntimeException e) {
            if ("Discount not found".equals(e.getMessage())) {
                return ResponseEntity.status(404).body(new ApiResponse<>("Discount not found"));
            }
            return ResponseEntity.badRequest().body(new ApiResponse<>("Failed to deactivate discount"));
        }
    }
}
