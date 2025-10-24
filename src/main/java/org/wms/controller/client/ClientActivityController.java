package org.wms.controller.client;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.wms.dto.client.ClientActivityDto;
import org.wms.service.client.ClientActivityService;
import org.wms.util.ApiResponse;

import java.util.List;
import java.util.Optional;

/**
 * Controller responsible for managing client activities, including listing,
 * retrieving by ID, creating, updating, and deleting activities.
 */
@Tag(name = "Client Activities", description = "Operations related to client activities")
@RestController
@RequestMapping("/api/activities")
public class ClientActivityController {

    @Autowired
    private ClientActivityService clientActivityService;

    @Operation(summary = "List all client activities", description = "Retrieve a list of all client activities")
    @GetMapping("/list")
    public ResponseEntity<?> listAll() {
        List<ClientActivityDto> list = clientActivityService.listAll();
        return ResponseEntity.ok(new ApiResponse<>("Client activities found", list));
    }

    @Operation(summary = "Get client activity by ID", description = "Retrieve a client activity by its ID")
    @GetMapping("/listById/{id}")
    public ResponseEntity<?> listById(@PathVariable Integer id) {
        Optional<ClientActivityDto> activity = clientActivityService.getById(id);
        return activity.map(a -> ResponseEntity.ok(new ApiResponse<>("Client activity found", a)))
                .orElseGet(() -> ResponseEntity.status(404)
                        .body(new ApiResponse<>("Client activity not found")));
    }

    @Operation(summary = "List activities by client", description = "Retrieve all activities associated with a client")
    @GetMapping("/listByClient/{clientId}")
    public ResponseEntity<?> listByClient(@PathVariable Integer clientId) {
        List<ClientActivityDto> activities = clientActivityService.listByClient(clientId);
        return ResponseEntity.ok(new ApiResponse<>("Client activities found", activities));
    }

    @Operation(summary = "Create client activity", description = "Create a new activity for a client")
    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody ClientActivityDto dto) {
        try {
            ClientActivityDto created = new ClientActivityDto(clientActivityService.create(dto));
            return ResponseEntity.ok(new ApiResponse<>("Client activity created successfully", created));
        } catch (RuntimeException e) {
            String msg = e.getMessage();
            if ("Client not found".equals(msg)) return ResponseEntity.status(404).body(new ApiResponse<>("Client not found"));
            if ("User not found".equals(msg)) return ResponseEntity.status(404).body(new ApiResponse<>("User not found"));
            return ResponseEntity.badRequest().body(new ApiResponse<>("Failed to create client activity: " + msg));
        }
    }

    @Operation(summary = "Update client activity", description = "Update an existing client activity")
    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody ClientActivityDto dto) {
        try {
            ClientActivityDto updated = new ClientActivityDto(clientActivityService.update(id, dto));
            return ResponseEntity.ok(new ApiResponse<>("Client activity updated successfully", updated));
        } catch (RuntimeException e) {
            if ("ClientActivity not found".equals(e.getMessage())) {
                return ResponseEntity.status(404).body(new ApiResponse<>("Client activity not found"));
            }
            return ResponseEntity.badRequest().body(new ApiResponse<>("Failed to update client activity"));
        }
    }

    @Operation(summary = "Delete client activity", description = "Delete an existing client activity")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        try {
            clientActivityService.delete(id);
            return ResponseEntity.ok(new ApiResponse<>("Client activity deleted successfully"));
        } catch (RuntimeException e) {
            if ("ClientActivity not found".equals(e.getMessage())) {
                return ResponseEntity.status(404).body(new ApiResponse<>("Client activity not found"));
            }
            return ResponseEntity.badRequest().body(new ApiResponse<>("Failed to delete client activity"));
        }
    }
}
