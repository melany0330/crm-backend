package org.wms.controller.sale;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.wms.dto.sale.SaleBillDto;
import org.wms.dto.purchases.DateRangeDto;
import org.wms.dto.sale.SaleDto;
import org.wms.service.sale.SaleService;
import org.wms.util.ApiResponse;

import java.util.List;
import java.util.Optional;

/**
 * Controller responsible for managing sales operations,
 * including listing, retrieving by ID, creating, and deactivating sales.
 */
@Tag(name = "Sales", description = "Operations related to sales and invoice registrations")
@RestController
@RequestMapping("/api/sales")
public class SaleController {

    @Autowired
    private SaleService saleService;

    /**
     * Retrieve a list of all active sales.
     *
     * @return List of SaleDto wrapped in ApiResponse.
     */
    @Operation(
            summary = "Get all sales",
            description = "Retrieve a list of all active sales"
    )
    @GetMapping("/list")
    public ResponseEntity<?> listAll() {
        List<SaleDto> list = saleService.listAll();
        return ResponseEntity.ok(new ApiResponse<>("Sales found", list));
    }

    /**
     * Retrieve a sale by its ID.
     *
     * @param idSale ID of the sale to retrieve.
     * @return SaleDto if found or 404 status if not.
     */
    @Operation(
            summary = "Get sale by ID",
            description = "Retrieve the details of a sale using its ID"
    )
    @GetMapping("/listById/{idSale}")
    public ResponseEntity<?> listById(@PathVariable Long idSale) {
        Optional<SaleDto> sale = saleService.listById(idSale);
        if (sale.isPresent()) {
            return ResponseEntity.ok(new ApiResponse<>("Sale found", sale.get()));
        } else {
            return ResponseEntity.status(404).body(new ApiResponse<>("Sale not found"));
        }
    }

    /**
     * Create a new sale with the provided data.
     * This also registers the corresponding invoice internally.
     *
     * @param dto Sale data transfer object containing sale details.
     * @return Created SaleBillDto or error message.
     */
    @Operation(
            summary = "Create sale",
            description = "Create a new sale and register the corresponding invoice"
    )
    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody SaleDto dto) {
        try {
            SaleBillDto saleBillDto = saleService.create(dto);
            return ResponseEntity.ok(new ApiResponse<>("Sale created successfully", saleBillDto));
        } catch (RuntimeException e) {
            e.printStackTrace(); // Log for debugging
            String msg = e.getMessage();
            if ("Client not found".equals(msg)) {
                return ResponseEntity.status(404).body(new ApiResponse<>("Client not found"));
            }
            if ("Product not found".equals(msg)) {
                return ResponseEntity.status(404).body(new ApiResponse<>("Product not found"));
            }
            return ResponseEntity.badRequest().body(new ApiResponse<>("Failed to create sale: " + msg));
        }
    }

    /**
     * Deactivate a sale by its ID.
     *
     * @param idSale ID of the sale to deactivate.
     * @return Success message or error.
     */
    @Operation(
            summary = "Deactivate sale",
            description = "Deactivate a sale by its ID"
    )
    @PutMapping("/deactivate/{idSale}")
    public ResponseEntity<?> deactivate(@PathVariable Long idSale) {
        try {
            saleService.deactivate(idSale);
            return ResponseEntity.ok(new ApiResponse<>("Sale deactivated successfully"));
        } catch (RuntimeException e) {
            if ("Sale not found".equals(e.getMessage())) {
                return ResponseEntity.status(404).body(new ApiResponse<>("Sale not found"));
            }
            return ResponseEntity.badRequest().body(new ApiResponse<>("Failed to deactivate sale"));
        }
    }

    @Operation(
            summary = "Get sales report by date range",
            description = "Retrieve a list of sales between two dates"
    )
    @PostMapping("/reportByDate")
    public ResponseEntity<?> reportByDate(@RequestBody DateRangeDto dateRange) {
        try {
            List<SaleDto> report = saleService.findSalesByDateRange(dateRange.getStartDate(), dateRange.getEndDate());
            return ResponseEntity.ok(new ApiResponse<>("Sales report generated", report));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>("Failed to generate report"));
        }
    }
    @Operation(summary = "Get sales by client",
            description = "Retrieve all active sales for a given client id")
    @GetMapping("/listByClient/{idClient}")
    public ResponseEntity<?> listByClient(@PathVariable Long idClient) {
        var list = saleService.listByClient(idClient);
        return ResponseEntity.ok(new ApiResponse<>("Sales found", list));
    }
}
