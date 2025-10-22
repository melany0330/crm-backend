package org.wms.service.campaign;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wms.dto.campaign.CampaignDto;
import org.wms.dto.campaign.CampaignClientDto;
import org.wms.model.campaign.Campaign;
import org.wms.model.campaign.CampaignClient;
import org.wms.model.client.Client;
import org.wms.repository.campaign.CampaignRepository;
import org.wms.repository.campaign.CampaignClientRepository;
import org.wms.repository.client.ClientRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CampaignService {

    @Autowired
    private CampaignRepository campaignRepository;

    @Autowired
    private CampaignClientRepository campaignClientRepository;

    @Autowired
    private ClientRepository clientRepository;

    // ==================== CAMPAIGNS ====================

    public List<CampaignDto> listAll() {
        return campaignRepository.findAll()
                .stream()
                .map(CampaignDto::new)
                .collect(Collectors.toList());
    }

    public Optional<CampaignDto> getById(Integer id) {
        return campaignRepository.findById(id).map(CampaignDto::new);
    }

    @Transactional
    public Campaign create(CampaignDto dto) {
        Campaign c = new Campaign();
        c.setCampaignName(dto.getName());
        c.setDescription(dto.getDescription());
        c.setStartDate(dto.getStartDate());
        c.setEndDate(dto.getEndDate());
        c.setBudget(dto.getBudget());
        c.setCampaignType(dto.getType());
        c.setObjective(dto.getObjective());
        c.setChannel(dto.getChannel());
        c.setStatus(dto.getStatus());
        c.setConversionRate(dto.getConversionRate());
        c.setCreatedAt(LocalDateTime.now());
        c.setUpdatedAt(LocalDateTime.now());
        return campaignRepository.save(c);
    }

    @Transactional
    public Campaign update(Integer id, CampaignDto dto) {
        Campaign c = campaignRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Campaign not found"));
        if (dto.getName() != null) c.setCampaignName(dto.getName());
        if (dto.getDescription() != null) c.setDescription(dto.getDescription());
        if (dto.getStartDate() != null) c.setStartDate(dto.getStartDate());
        if (dto.getEndDate() != null) c.setEndDate(dto.getEndDate());
        if (dto.getBudget() != null) c.setBudget(dto.getBudget());
        if (dto.getType() != null) c.setCampaignType(dto.getType());
        if (dto.getObjective() != null) c.setObjective(dto.getObjective());
        if (dto.getChannel() != null) c.setChannel(dto.getChannel());
        if (dto.getStatus() != null) c.setStatus(dto.getStatus());
        if (dto.getConversionRate() != null) c.setConversionRate(dto.getConversionRate());
        c.setUpdatedAt(LocalDateTime.now());
        return campaignRepository.save(c);
    }

    @Transactional
    public void deactivate(Integer id) {
        Campaign c = campaignRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Campaign not found"));
        c.setStatus("Inactive");
        c.setUpdatedAt(LocalDateTime.now());
        campaignRepository.save(c);
    }

    // ==================== CAMPAIGN CLIENTS ====================

    public List<CampaignClientDto> listClients(Integer campaignId) {
        return campaignClientRepository.findByCampaign_IdCampaign(campaignId)
                .stream()
                .map(CampaignClientDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public CampaignClient addClient(CampaignClientDto dto) {
        Campaign c = campaignRepository.findById(dto.getCampaignId())
                .orElseThrow(() -> new RuntimeException("Campaign not found"));
        Client client = clientRepository.findById(dto.getClientId())
                .orElseThrow(() -> new RuntimeException("Client not found"));
        CampaignClient cc = new CampaignClient();
        cc.setCampaign(c);
        cc.setClient(client);
        cc.setResult(dto.getResult());
        cc.setInteractionDate(dto.getInteractionDate());
        cc.setComments(dto.getComments());
        return campaignClientRepository.save(cc);
    }

    @Transactional
    public CampaignClient updateClient(Integer id, CampaignClientDto dto) {
        CampaignClient cc = campaignClientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("CampaignClient not found"));
        if (dto.getResult() != null) cc.setResult(dto.getResult());
        if (dto.getInteractionDate() != null) cc.setInteractionDate(dto.getInteractionDate());
        if (dto.getComments() != null) cc.setComments(dto.getComments());
        return campaignClientRepository.save(cc);
    }

    @Transactional
    public void removeClient(Integer id) {
        CampaignClient cc = campaignClientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("CampaignClient not found"));
        campaignClientRepository.delete(cc);
    }
}
