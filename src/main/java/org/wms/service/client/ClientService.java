package org.wms.service.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wms.dto.client.ClientDto;
import org.wms.model.client.Client;
import org.wms.repository.client.ClientRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    public List<ClientDto> listAll() {
        return clientRepository.findAll()
                .stream()
                .map(ClientDto::new)
                .toList();
    }

    public Optional<ClientDto> listById(Integer idClient) {
        return clientRepository.findById(idClient).map(ClientDto::new);
    }
    public Optional<ClientDto> findByNit(String nit) {
        return clientRepository.findByNit(nit).map(ClientDto::new);
    }

    public Client create(ClientDto dto) {
        Client newClient = new Client();
        newClient.setNit(dto.getNit());
        newClient.setFirstName(dto.getFirstName());
        newClient.setLastName(dto.getLastName());
        newClient.setEmail(dto.getEmail());
        newClient.setPhone(dto.getPhone());
        newClient.setAddress(dto.getAddress());
        newClient.setCreatedAt(LocalDateTime.now());
        newClient.setUpdatedAt(LocalDateTime.now());
        return clientRepository.save(newClient);
    }

    public Client update(Integer idClient, ClientDto dto) {
        Client updatedClient = clientRepository.findById(idClient)
                .orElseThrow(() -> new RuntimeException("Client not found"));
        updatedClient.setNit(dto.getNit());
        updatedClient.setFirstName(dto.getFirstName());
        updatedClient.setLastName(dto.getLastName());
        updatedClient.setEmail(dto.getEmail());
        updatedClient.setPhone(dto.getPhone());
        updatedClient.setAddress(dto.getAddress());
        updatedClient.setUpdatedAt(LocalDateTime.now());
        return clientRepository.save(updatedClient);
    }

}
