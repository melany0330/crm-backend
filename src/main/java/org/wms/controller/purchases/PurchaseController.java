package org.wms.controller.purchases;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.wms.dto.purchases.*;
import org.wms.model.purchases.Purchase;
import org.wms.service.purchases.PurchaseService;
import org.wms.util.ApiResponse;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/purchases")
@Tag(name = "Purchases", description = "Routes that manage Purchases")
public class PurchaseController {
    @Autowired
    private PurchaseService purchaseService;

    @Operation(summary = "Get all purchases")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Purchases found",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = PurchaseBillDto.class))
    )
    @GetMapping("/list")
    public ResponseEntity<?> listAll() {
        List<PurchaseBillDto> list = purchaseService.listAll();
        return ResponseEntity.ok(new ApiResponse<>("Purchases found",list));
    }
    @Operation(summary = "Get purchase by ID")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Purchase found",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = PurchaseBillDto.class))
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Purchase not found",
            content = @Content
    )

    @GetMapping("/listById/{idPurchase}")
    public ResponseEntity<?> listById(@PathVariable Integer idPurchase){
        Optional<PurchaseBillDto> purchase = purchaseService.listById(idPurchase);

        if(purchase.isPresent()){
            return ResponseEntity.ok(new ApiResponse<>("Purchase found", purchase.get()));
        }
        else {
            return ResponseEntity.status(404).body(new ApiResponse<>("Purchase not foud"));
        }
    }

    @Operation(summary = "Create a new purchase")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Purchase created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PurchaseBillDto.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Failed to create purchase",
                    content = @Content
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Provider/Product/User not found or mismatched totals",
                    content = @Content
            )
    })
    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody PurchaseRequestDto dto) {
        try {
            PurchaseBillDto responseDto = purchaseService.create(dto);
            return ResponseEntity.ok(new ApiResponse<>("Purchase created successfully", responseDto));
        } catch (RuntimeException e) {
            if (e.getMessage().equals("Provider not found")) {
                return ResponseEntity.status(404).body(
                        new ApiResponse<>("Provider not found")
                );
            }
            if (e.getMessage().equals("Product not found")) {
                return ResponseEntity.status(404).body(
                        new ApiResponse<>("Product not found")
                );
            }
            if (e.getMessage().equals("User not found")) {
                return ResponseEntity.status(404).body(
                        new ApiResponse<>("User not found")
                );
            }
            if (e.getMessage().equals("The total and the details don't match")) {
                return ResponseEntity.status(404).body(
                        new ApiResponse<>("The total and the details don't match")
                );
            }

            return ResponseEntity.badRequest().body(
                    new ApiResponse<>("Failed to create purchase")
            );
        }
    }

    @Operation(summary = "Get purchase report")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Report generated",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = PurchaseBillDto.class))
    )

    @PostMapping("/reportByDates")
    public ResponseEntity<?> reportByDateRange(@RequestBody DateRangeDto range){
        List<PurchaseBillDto> report = purchaseService.getReportByDateRange(
                range.getStartDate(), range.getEndDate());
        return ResponseEntity.ok(new ApiResponse<>("Report generated", report));
    }

    @Operation(summary = "Deactivate a purchase by ID")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Purchase deactivated successfully",
                    content = @Content
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Purchase not found",
                    content = @Content
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Failed to deactivate purchase",
                    content = @Content
            )
    })

    @PutMapping("/deactivate/{idPurchase}")
    public ResponseEntity<?> deactivate(@PathVariable Integer idPurchase){
        try{
            purchaseService.deactivate((idPurchase));
            return ResponseEntity.ok(new ApiResponse<>("Purchase deactivated successfully"));
        } catch(RuntimeException e){
            if(e.getMessage().equals("Purchase not found")){
                return ResponseEntity.status(404).body(
                        new ApiResponse<>("Purchase not found")
                );
            }
            return ResponseEntity.badRequest().body(
              new ApiResponse<>("Failed to deactivate purchase")
            );
        }
    }


}
