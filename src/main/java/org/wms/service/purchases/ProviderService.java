package org.wms.service.purchases;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wms.dto.purchases.ProviderDto;
import org.wms.model.purchases.Provider;
import org.wms.repository.purchases.ProviderRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ProviderService {

    @Autowired
    private ProviderRepository providerRepository;

    public List<ProviderDto> listAll() {
        return providerRepository.findAll()
                .stream()
                .filter(Provider::getStatus)
                .map(ProviderDto::new)
                .toList();
    }

    public Optional<ProviderDto> listById(Integer idProvider) {
        return providerRepository.findById(idProvider).map(ProviderDto::new);
    }

    public Provider create(ProviderDto dto)
    {
        Provider newProvider = new Provider();
        newProvider.setNit(dto.getNit());
        newProvider.setName(dto.getName());
        newProvider.setEmail(dto.getEmail());
        newProvider.setPhone(dto.getPhone());
        newProvider.setAddress(dto.getAddress());
        newProvider.setStatus(true);
        newProvider.setCreatedAt(java.time.LocalDateTime.now());
        newProvider.setUpdatedAt(java.time.LocalDateTime.now());
        return providerRepository.save(newProvider);
    }

    public Provider update(Integer idProvider, ProviderDto dto) {

        Provider updatedProvider = providerRepository.findById(idProvider)
                .orElseThrow(() -> new RuntimeException("Provider not found"));
        updatedProvider.setNit(dto.getNit());
        updatedProvider.setName(dto.getName());
        updatedProvider.setEmail(dto.getEmail());
        updatedProvider.setPhone(dto.getPhone());
        updatedProvider.setAddress(dto.getAddress());
        updatedProvider.setUpdatedAt(java.time.LocalDateTime.now());

        return providerRepository.save(updatedProvider);
    }

    public void deactivate(Integer idProvider) {
        Provider deactivatedProvider = providerRepository.findById(idProvider)
                .orElseThrow(() -> new RuntimeException("Provider not found"));

        deactivatedProvider.setStatus(false);
        deactivatedProvider.setUpdatedAt(java.time.LocalDateTime.now());

        providerRepository.save(deactivatedProvider);
    }
}