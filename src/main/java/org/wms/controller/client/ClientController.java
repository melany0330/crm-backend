package org.wms.controller.client;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.wms.dto.client.ClientDto;
import org.wms.model.client.Client;
import org.wms.service.client.ClientService;
import org.wms.util.ApiResponse;


import java.util.List;
import java.util.Optional;

/**
 * Controller responsible for managing all client-related operations in the system.
 * This includes listing all clients, retrieving a client by ID, creating new clients,
 * and updating existing client information.
 */
@Tag(name = "Clients", description = "Operations related to system clients")
@RestController
@RequestMapping("/api/clients")
public class ClientController {

    @Autowired
    private ClientService clientService;

    /**
     * Retrieves a list of all registered clients in the system.
     *
     * @return List of ClientDto wrapped in a custom ApiResponse.
     */
    @Operation(
            summary = "Get all clients",
            description = "Retrieves a list of all clients registered in the system."
    )
    @GetMapping("/list")
    public ResponseEntity<?> listAll() {
        List<ClientDto> list = clientService.listAll();
        return ResponseEntity.ok(new ApiResponse<>("Clients found", list));
    }

    /**
     * Retrieves the client data by its unique identifier.
     *
     * @param idClient ID of the client to be retrieved.
     * @return ClientDto if found, otherwise 404 response.
     */
    @Operation(
            summary = "Get client by ID",
            description = "Retrieves the details of a specific client using their unique ID."
    )
    @GetMapping("/listById/{idClient}")
    public ResponseEntity<?> listById(@PathVariable Integer idClient) {
        Optional<ClientDto> client = clientService.listById(idClient);
        if (client.isPresent()) {
            return ResponseEntity.ok(new ApiResponse<>("Client found", client.get()));
        } else {
            return ResponseEntity.status(404).body(new ApiResponse<>("Client not found"));
        }
    }
    @Operation(
            summary = "Get client by Nit number",
            description = "Retrieves the details of a specific client using their NIT."
    )
    @GetMapping("/ByNit/{nit}")
    public ResponseEntity<ClientDto> getByNIT(@PathVariable String nit) {
        return clientService.findByNit(nit)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Creates a new client using the provided data.
     *
     * @param dto Data Transfer Object containing client data.
     * @return The created client with a success message, or error if failed.
     */
    @Operation(
            summary = "Create new client",
            description = "Creates a new client using the provided data."
    )
    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody ClientDto dto) {
        try {
            Client newClient = clientService.create(dto);
            ClientDto responseDto = new ClientDto(newClient);
            return ResponseEntity.ok(new ApiResponse<>("Client successfully created", responseDto));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(
                    new ApiResponse<>("Failed to create client")
            );
        }
    }

    /**
     * Updates an existing client's data based on their ID.
     *
     * @param idClient ID of the client to be updated.
     * @param dto Data Transfer Object containing updated client data.
     * @return Updated client data or appropriate error message.
     */
    @Operation(
            summary = "Update client",
            description = "Updates the data of an existing client using their ID."
    )
    @PutMapping("/update/{idClient}")
    public ResponseEntity<?> update(@PathVariable Integer idClient, @RequestBody ClientDto dto) {
        try {
            Client updatedClient = clientService.update(idClient, dto);
            ClientDto responseDto = new ClientDto(updatedClient);
            return ResponseEntity.ok(new ApiResponse<>("Client successfully updated", responseDto));
        } catch (RuntimeException e) {
            if ("Client not found".equals(e.getMessage())) {
                return ResponseEntity.status(404).body(
                        new ApiResponse<>("Client not found")
                );
            }
            return ResponseEntity.badRequest().body(
                    new ApiResponse<>("Failed to update client")
            );
        }
    }
}
