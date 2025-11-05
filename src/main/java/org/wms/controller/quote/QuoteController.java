package org.wms.controller.quote;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.wms.dto.quote.QuoteDto;
import org.wms.service.quote.QuoteService;
import org.wms.util.ApiResponse;

import java.util.List;
import java.util.Optional;

@Tag(name = "Quotes", description = "Operations related to client quotes and quotation details")
@RestController
@RequestMapping("/api/quotes")
@RequiredArgsConstructor
public class QuoteController {

    private final QuoteService quoteService;

    @Operation(summary = "Get all quotes")
    @GetMapping("/list")
    public ResponseEntity<?> listAll() {
        List<QuoteDto> list = quoteService.listAll();
        return ResponseEntity.ok(new ApiResponse<>("Quotes found", list));
    }

    @Operation(summary = "Get quote by ID")
    @GetMapping("/listById/{idQuote}")
    public ResponseEntity<?> listById(@PathVariable Integer idQuote) {
        Optional<QuoteDto> q = quoteService.listById(idQuote);
        return q.<ResponseEntity<?>>map(quote ->
                        ResponseEntity.ok(new ApiResponse<>("Quote found", quote)))
                .orElseGet(() -> ResponseEntity.status(404)
                        .body(new ApiResponse<>("Quote not found")));
    }

    @Operation(summary = "Create quote")
    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody QuoteDto dto) {
        QuoteDto created = quoteService.create(dto);
        return ResponseEntity.ok(new ApiResponse<>("Quote created successfully", created));
    }

    @Operation(summary = "Update quote status")
    @PutMapping("/updateStatus/{idQuote}")
    public ResponseEntity<?> updateStatus(@PathVariable Integer idQuote,
                                          @RequestBody QuoteDto body) {
        quoteService.updateStatus(idQuote, body.getStatus());
        return ResponseEntity.ok(new ApiResponse<>("Quote status updated"));
    }

    @Operation(summary = "Get quotes by client",
            description = "Retrieve all active quotes for a given client id")
    @GetMapping("/listByClient/{idClient}")
    public ResponseEntity<?> listByClient(@PathVariable Integer idClient) {
        List<QuoteDto> list = quoteService.listByClient(idClient);
        return ResponseEntity.ok(new ApiResponse<>("Quotes found", list));
    }
}
