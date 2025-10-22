package org.wms.controller.campaign;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.wms.dto.campaign.CampaignDto;
import org.wms.dto.campaign.CampaignClientDto;
import org.wms.model.campaign.Campaign;
import org.wms.model.campaign.CampaignClient;
import org.wms.service.campaign.CampaignService;
import org.wms.util.ApiResponse;

import java.util.List;
import java.util.Optional;

@Tag(name = "Campaigns", description = "Manage marketing campaigns and client interactions")
@RestController
@RequestMapping("/api/campaigns")
public class CampaignController {

    @Autowired
    private CampaignService campaignService;

    // ==================== CAMPAIGNS ====================

    @Operation(summary = "List all campaigns", description = "Retrieve all campaigns")
    @GetMapping("/list")
    public ResponseEntity<?> listAll() {
        List<CampaignDto> list = campaignService.listAll();
        return ResponseEntity.ok(new ApiResponse<>("Campaigns found", list));
    }

    @Operation(summary = "Get campaign by ID", description = "Retrieve a campaign using its ID")
    @GetMapping("/listById/{id}")
    public ResponseEntity<?> listById(@PathVariable Integer id) {
        Optional<CampaignDto> campaign = campaignService.getById(id);
        return campaign.map(c -> ResponseEntity.ok(new ApiResponse<>("Campaign found", c)))
                .orElseGet(() -> ResponseEntity.status(404).body(new ApiResponse<>("Campaign not found")));
    }

    @Operation(summary = "Create campaign", description = "Create a new marketing campaign")
    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody CampaignDto dto) {
        try {
            Campaign created = campaignService.create(dto);
            return ResponseEntity.ok(new ApiResponse<>("Campaign created successfully", new CampaignDto(created)));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>("Failed to create campaign: " + e.getMessage()));
        }
    }

    @Operation(summary = "Update campaign", description = "Update an existing campaign")
    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody CampaignDto dto) {
        try {
            Campaign updated = campaignService.update(id, dto);
            return ResponseEntity.ok(new ApiResponse<>("Campaign updated successfully", new CampaignDto(updated)));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(new ApiResponse<>(e.getMessage()));
        }
    }

    @Operation(summary = "Deactivate campaign", description = "Set campaign status to 'Inactive'")
    @PutMapping("/deactivate/{id}")
    public ResponseEntity<?> deactivate(@PathVariable Integer id) {
        try {
            campaignService.deactivate(id);
            return ResponseEntity.ok(new ApiResponse<>("Campaign deactivated successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(new ApiResponse<>(e.getMessage()));
        }
    }

    // ==================== CAMPAIGN CLIENTS ====================

    @Operation(summary = "List clients of a campaign", description = "Retrieve all clients associated with a campaign")
    @GetMapping("/{campaignId}/clients")
    public ResponseEntity<?> listClients(@PathVariable Integer campaignId) {
        List<CampaignClientDto> list = campaignService.listClients(campaignId);
        return ResponseEntity.ok(new ApiResponse<>("Campaign clients found", list));
    }

    @Operation(summary = "Add client to campaign", description = "Record a client's participation or interaction in a campaign")
    @PostMapping("/clients/add")
    public ResponseEntity<?> addClient(@RequestBody CampaignClientDto dto) {
        try {
            CampaignClient created = campaignService.addClient(dto);
            return ResponseEntity.ok(new ApiResponse<>("Client added to campaign successfully", new CampaignClientDto(created)));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>("Failed to add client: " + e.getMessage()));
        }
    }

    @Operation(summary = "Update campaign client", description = "Update interaction details of a client in a campaign")
    @PutMapping("/clients/update/{id}")
    public ResponseEntity<?> updateClient(@PathVariable Integer id, @RequestBody CampaignClientDto dto) {
        try {
            CampaignClient updated = campaignService.updateClient(id, dto);
            return ResponseEntity.ok(new ApiResponse<>("Campaign client updated successfully", new CampaignClientDto(updated)));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(new ApiResponse<>(e.getMessage()));
        }
    }

    @Operation(summary = "Remove client from campaign", description = "Delete a client record from a campaign")
    @DeleteMapping("/clients/remove/{id}")
    public ResponseEntity<?> removeClient(@PathVariable Integer id) {
        try {
            campaignService.removeClient(id);
            return ResponseEntity.ok(new ApiResponse<>("Client removed from campaign successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(new ApiResponse<>(e.getMessage()));
        }
    }
}
