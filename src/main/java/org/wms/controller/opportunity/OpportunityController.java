package org.wms.controller.opportunity;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.wms.dto.opportunity.OpportunityDto;
import org.wms.model.opportunity.Opportunity;
import org.wms.service.opportunity.OpportunityService;
import org.wms.util.ApiResponse;

import java.util.List;
import java.util.Optional;

/**
 * Controller responsible for managing sales opportunities.
 * Includes operations to list, create, update, update status, and deactivate opportunities.
 */
@Tag(name = "Opportunities", description = "Operations related to sales opportunities")
@RestController
@RequestMapping("/api/opportunities")
public class OpportunityController {

    @Autowired
    private OpportunityService opportunityService;

    @Operation(
            summary = "Get all opportunities",
            description = "Retrieve a list of all sales opportunities"
    )
    @GetMapping("/list")
    public ResponseEntity<?> listAll() {
        List<OpportunityDto> list = opportunityService.listAll();
        return ResponseEntity.ok(new ApiResponse<>("Opportunities found", list));
    }

    @Operation(
            summary = "Get opportunities by client",
            description = "Retrieve sales opportunities associated with a specific client id"
    )
    @GetMapping("/listByClient/{idClient}")
    public ResponseEntity<?> listByClient(@PathVariable Integer idClient) {
        List<OpportunityDto> list = opportunityService.listByClient(idClient);
        return ResponseEntity.ok(new ApiResponse<>("Opportunities found", list));
    }

    @Operation(
            summary = "Get opportunities by status",
            description = "Retrieve opportunities filtered by their status"
    )
    @GetMapping("/listByStatus/{status}")
    public ResponseEntity<?> listByStatus(@PathVariable String status) {
        List<OpportunityDto> list = opportunityService.listByStatus(status);
        return ResponseEntity.ok(new ApiResponse<>("Opportunities found", list));
    }

    @Operation(
            summary = "Suggested opportunities",
            description = "Returns active opportunities that can be used as potential client suggestions"
    )
    @GetMapping("/suggestions")
    public ResponseEntity<?> suggestions() {
        List<OpportunityDto> list = opportunityService.listSuggestions();
        return ResponseEntity.ok(new ApiResponse<>("Suggestions ready", list));
    }

    @Operation(
            summary = "Get opportunity by ID",
            description = "Retrieve the details of an opportunity using its ID"
    )
    @GetMapping("/listById/{id}")
    public ResponseEntity<?> listById(@PathVariable Integer id) {
        Optional<OpportunityDto> opportunity = opportunityService.listById(id);
        if (opportunity.isPresent()) {
            return ResponseEntity.ok(new ApiResponse<>("Opportunity found", opportunity.get()));
        } else {
            return ResponseEntity.status(404).body(new ApiResponse<>("Opportunity not found"));
        }
    }

    @Operation(
            summary = "Create opportunity",
            description = "Create a new sales opportunity with client, user, optional quote, and details"
    )
    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody OpportunityDto dto) {
        try {
            Opportunity opportunity = opportunityService.create(dto);
            OpportunityDto responseDto = new OpportunityDto(opportunity);
            return ResponseEntity.ok(new ApiResponse<>("Opportunity created successfully", responseDto));
        } catch (RuntimeException e) {
            String msg = e.getMessage();
            if ("Client not found".equals(msg)) {
                return ResponseEntity.status(404).body(new ApiResponse<>("Client not found"));
            }
            if ("User not found".equals(msg)) {
                return ResponseEntity.status(404).body(new ApiResponse<>("User not found"));
            }
            if ("Quote not found".equals(msg)) {
                return ResponseEntity.status(404).body(new ApiResponse<>("Quote not found"));
            }
            return ResponseEntity.badRequest().body(new ApiResponse<>("Failed to create opportunity: " + msg));
        }
    }

    @Operation(
            summary = "Update opportunity",
            description = "Update an existing sales opportunity"
    )
    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody OpportunityDto dto) {
        try {
            Opportunity updated = opportunityService.update(id, dto);
            OpportunityDto responseDto = new OpportunityDto(updated);
            return ResponseEntity.ok(new ApiResponse<>("Opportunity updated successfully", responseDto));
        } catch (RuntimeException e) {
            String msg = e.getMessage();
            if ("Opportunity not found".equals(msg)) {
                return ResponseEntity.status(404).body(new ApiResponse<>("Opportunity not found"));
            }
            if ("Client not found".equals(msg)) {
                return ResponseEntity.status(404).body(new ApiResponse<>("Client not found"));
            }
            if ("User not found".equals(msg)) {
                return ResponseEntity.status(404).body(new ApiResponse<>("User not found"));
            }
            if ("Quote not found".equals(msg)) {
                return ResponseEntity.status(404).body(new ApiResponse<>("Quote not found"));
            }
            return ResponseEntity.badRequest().body(new ApiResponse<>("Failed to update opportunity: " + msg));
        }
    }

    @Operation(
            summary = "Update opportunity status",
            description = "Update the status of an existing opportunity"
    )
    @PutMapping("/updateStatus/{id}")
    public ResponseEntity<?> updateStatus(@PathVariable Integer id, @RequestBody OpportunityDto dto) {
        try {
            String newStatus = dto.getStatus();
            if (newStatus == null || newStatus.isBlank()) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse<>("New status must be provided"));
            }
            opportunityService.updateStatus(id, newStatus);
            return ResponseEntity.ok(new ApiResponse<>("Opportunity status updated successfully"));
        } catch (RuntimeException e) {
            if ("Opportunity not found".equals(e.getMessage())) {
                return ResponseEntity.status(404).body(new ApiResponse<>("Opportunity not found"));
            }
            return ResponseEntity.badRequest().body(new ApiResponse<>("Failed to update opportunity status"));
        }
    }

    @Operation(
            summary = "Deactivate opportunity",
            description = "Deactivate (soft delete) an opportunity by ID"
    )
    @PutMapping("/deactivate/{id}")
    public ResponseEntity<?> deactivate(@PathVariable Integer id) {
        try {
            opportunityService.deactivate(id);
            return ResponseEntity.ok(new ApiResponse<>("Opportunity deactivated successfully"));
        } catch (RuntimeException e) {
            if ("Opportunity not found".equals(e.getMessage())) {
                return ResponseEntity.status(404).body(new ApiResponse<>("Opportunity not found"));
            }
            return ResponseEntity.badRequest().body(new ApiResponse<>("Failed to deactivate opportunity"));
        }
    }
}
