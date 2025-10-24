package org.wms.service.client;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wms.dto.client.ClientActivityDto;
import org.wms.model.client.ClientActivity;
import org.wms.model.auth.User;
import org.wms.model.client.Client;
import org.wms.repository.client.ClientActivityRepository;
import org.wms.repository.auth.UserRepository;
import org.wms.repository.client.ClientRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ClientActivityService {

    @Autowired
    private ClientActivityRepository clientActivityRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Listar todas las actividades registradas
     */
    public List<ClientActivityDto> listAll() {
        return clientActivityRepository.findAll()
                .stream()
                .map(ClientActivityDto::new)
                .collect(Collectors.toList());
    }

    /**
     * Buscar una actividad por ID
     */
    public Optional<ClientActivityDto> getById(Integer id) {
        return clientActivityRepository.findById(id).map(ClientActivityDto::new);
    }

    /**
     * Listar actividades por cliente
     */
    public List<ClientActivityDto> listByClient(Integer clientId) {
        return clientActivityRepository.findByClientIdClient(clientId)
                .stream()
                .map(ClientActivityDto::new)
                .collect(Collectors.toList());
    }


    /**
     * Crear una nueva actividad
     */
    @Transactional
    public ClientActivity create(ClientActivityDto dto) {
        Client client = clientRepository.findById(dto.getClientId())
                .orElseThrow(() -> new RuntimeException("Client not found"));

        User user = userRepository.findById(dto.getUserId().longValue())
                .orElseThrow(() -> new RuntimeException("User not found"));

        ClientActivity activity = new ClientActivity();
        activity.setClient(client);
        activity.setUser(user);
        activity.setActivityType(dto.getActivityType());
        activity.setDescription(dto.getDescription());
        activity.setActivityDate(dto.getActivityDate());
        activity.setCreatedAt(LocalDateTime.now());
        activity.setUpdatedAt(LocalDateTime.now());

        return clientActivityRepository.save(activity);
    }

    /**
     * Actualizar una actividad existente
     */
    @Transactional
    public ClientActivity update(Integer id, ClientActivityDto dto) {
        ClientActivity activity = clientActivityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ClientActivity not found"));

        if (dto.getActivityType() != null) activity.setActivityType(dto.getActivityType());
        if (dto.getDescription() != null) activity.setDescription(dto.getDescription());
        if (dto.getActivityDate() != null) activity.setActivityDate(dto.getActivityDate());
        activity.setUpdatedAt(LocalDateTime.now());

        return clientActivityRepository.save(activity);
    }

    /**
     * Eliminar una actividad ()
     */
    @Transactional
    public void delete(Integer id) {
        ClientActivity activity = clientActivityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ClientActivity not found"));
        clientActivityRepository.delete(activity);
    }
}
