package org.wms.controller.quote;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.wms.dto.quote.QuoteDto;
import org.wms.service.quote.QuoteService;
import org.wms.util.ApiResponse;

import java.util.List;
import java.util.Optional;

/**
 * Controller responsible for managing quote operations,
 * including listing, retrieving by ID, creating, and updating status.
 */
@Tag(name = "Quotes", description = "Operations related to client quotes and quotation details")
@RestController
@RequestMapping("/api/quotes")
public class QuoteController {

    @Autowired
    private QuoteService quoteService;

    /**
     * Retrieve a list of all quotes.
     *
     * @return List of QuoteDto wrapped in ApiResponse.
     */
    @Operation(
            summary = "Get all quotes",
            description = "Retrieve a list of all registered quotes"
    )
    @GetMapping("/list")
    public ResponseEntity<?> listAll() {
        List<QuoteDto> list = quoteService.listAll();
        return ResponseEntity.ok(new ApiResponse<>("Quotes found", list));
    }

    /**
     * Retrieve a quote by its ID.
     *
     * @param idQuote ID of the quote to retrieve.
     * @return QuoteDto if found or 404 status if not.
     */
    @Operation(
            summary = "Get quote by ID",
            description = "Retrieve the details of a quote using its ID"
    )
    @GetMapping("/listById/{idQuote}")
    public ResponseEntity<?> listById(@PathVariable Integer idQuote) {
        Optional<QuoteDto> quote = quoteService.listById(idQuote);
        if (quote.isPresent()) {
            return ResponseEntity.ok(new ApiResponse<>("Quote found", quote.get()));
        } else {
            return ResponseEntity.status(404).body(new ApiResponse<>("Quote not found"));
        }
    }

    /**
     * Create a new quote with the provided data.
     *
     * @param dto Quote data transfer object containing quote details.
     * @return Created QuoteDto or error message.
     */
    @Operation(
            summary = "Create quote",
            description = "Create a new quote with details and total calculation"
    )
    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody QuoteDto dto) {
        try {
            QuoteDto createdQuote = quoteService.create(dto);
            return ResponseEntity.ok(new ApiResponse<>("Quote created successfully", createdQuote));
        } catch (RuntimeException e) {
            e.printStackTrace(); // For debugging
            String msg = e.getMessage();
            if ("Client not found".equals(msg)) {
                return ResponseEntity.status(404).body(new ApiResponse<>("Client not found"));
            }
            if ("Product not found".equals(msg)) {
                return ResponseEntity.status(404).body(new ApiResponse<>("Product not found"));
            }
            if ("User not found".equals(msg)) {
                return ResponseEntity.status(404).body(new ApiResponse<>("User not found"));
            }
            return ResponseEntity.badRequest().body(new ApiResponse<>("Failed to create quote: " + msg));
        }
    }
    @Operation(
            summary = "Update quote status",
            description = "Update the status of an existing quote. Only the 'status' field of the QuoteDto is used."
    )
    @PutMapping("/updateStatus/{idQuote}")
    public ResponseEntity<?> updateStatus(
            @PathVariable Integer idQuote,
            @RequestBody QuoteDto dto
    ) {
        try {
            // Tomamos solo el status del DTO
            String newStatus = dto.getStatus();
            if (newStatus == null || newStatus.isBlank()) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse<>("New status must be provided"));
            }

            quoteService.updateStatus(idQuote, newStatus);
            return ResponseEntity.ok(new ApiResponse<>("Quote status updated successfully"));
        } catch (RuntimeException e) {
            if ("Quote not found".equals(e.getMessage())) {
                return ResponseEntity.status(404).body(new ApiResponse<>("Quote not found"));
            }
            return ResponseEntity.badRequest().body(new ApiResponse<>("Failed to update quote status"));
        }
    }

}
