package org.wms.controller.purchases;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.wms.dto.purchases.ProviderDto;
import org.wms.model.auth.TokenResponse;
import org.wms.model.purchases.Provider;
import org.wms.service.purchases.ProviderService;
import org.wms.util.ApiResponse;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/providers")
@Tag(name = "Providers", description = "Routes for provider management")
public class ProviderController {

    @Autowired
    private ProviderService providerService;


    @Operation(summary = "Get all providers")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Providers found",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ProviderDto.class))
    )
    @GetMapping("/list")
    public ResponseEntity<?> listAll() {
        List<ProviderDto> list = providerService.listAll();
        return ResponseEntity.ok(new ApiResponse<>("Providers found", list));
    }

    @Operation(summary = "Get provider by ID")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Provider found",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ProviderDto.class))
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Provider not found",
            content = @Content
    )

    @GetMapping("listById/{idProvider}")
    public ResponseEntity<?> listById(@PathVariable Integer idProvider) {
        Optional<ProviderDto> provider = providerService.listById(idProvider);

        if (provider.isPresent()) {
            return ResponseEntity.ok(new ApiResponse<>("Provider found", provider.get()));
        } else {
            return ResponseEntity.status(404).body(new ApiResponse<>("Provider not found"));
        }
    }

    @Operation(summary = "Create a new provider")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Provider created successfully",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ProviderDto.class))
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "Failed to create provider",
            content = @Content
    )

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody ProviderDto dto) {
        try {
            Provider newProvider = providerService.create(dto);
            ProviderDto responseDto = new ProviderDto(newProvider);
            return ResponseEntity.ok(new ApiResponse<>("Provider created successfully", responseDto));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(
                    new ApiResponse<>("Failed to create provider")
            );
        }
    }

    @Operation(summary = "Update provider by ID")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Provider updated successfully",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ProviderDto.class))
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Provider not found",
            content = @Content
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "Failed to update provider",
            content = @Content
    )

    @PutMapping("/update/{idProvider}")
    public ResponseEntity<?> update(@PathVariable Integer idProvider, @RequestBody ProviderDto dto) {
        try {
            Provider updatedProvider = providerService.update(idProvider, dto);
            ProviderDto responseDto = new ProviderDto(updatedProvider);
            return ResponseEntity.ok(new ApiResponse<>("Provider updated successfully", responseDto));
        } catch (RuntimeException e) {
            if (e.getMessage().equals("Provider not found")) {
                return ResponseEntity.status(404).body(
                        new ApiResponse<>("Provider not found")
                );
            }
            return ResponseEntity.badRequest().body(
                    new ApiResponse<>("Failed to update provider")
            );
        }
    }

    @Operation(summary = "Deactivate a provider by ID")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Provider deactivated successfully",
            content = @Content
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Provider not found",
            content = @Content
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "Failed to deactivate provider",
            content = @Content
    )

    @PutMapping("/deactivate/{idProvider}")
    public ResponseEntity<?> deactivate(@PathVariable Integer idProvider) {
        try {
            providerService.deactivate(idProvider);
            return ResponseEntity.ok(new ApiResponse<>("Provider deactivated successfully"));
        } catch (RuntimeException e) {
            if (e.getMessage().equals("Provider not found")) {
                return ResponseEntity.status(404).body(
                        new ApiResponse<>("Provider not found")
                );
            }
            return ResponseEntity.badRequest().body(
                    new ApiResponse<>("Failed to deactivate provider")
            );
        }
    }
}