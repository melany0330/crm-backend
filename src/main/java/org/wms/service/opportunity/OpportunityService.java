package org.wms.service.opportunity;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wms.dto.opportunity.OpportunityDto;
import org.wms.model.opportunity.Opportunity;
import org.wms.model.client.Client;
import org.wms.model.quote.Quote;
import org.wms.model.auth.User;
import org.wms.repository.opportunity.OpportunityRepository;
import org.wms.repository.client.ClientRepository;
import org.wms.repository.quote.QuoteRepository;
import org.wms.repository.auth.UserRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OpportunityService {

    @Autowired
    private OpportunityRepository opportunityRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private QuoteRepository quoteRepository;

    @Autowired
    private UserRepository userRepository;

    // List all active opportunities (optional: filter by status "Eliminated" or similar)
    public List<OpportunityDto> listAll() {
        return opportunityRepository.findAll()
                .stream()
                .map(OpportunityDto::new)
                .collect(Collectors.toList());
    }

    public List<OpportunityDto> listByClient(Integer clientId) {
        return opportunityRepository.findByClient_IdClient(clientId)
                .stream()
                .map(OpportunityDto::new)
                .collect(Collectors.toList());
    }

    public List<OpportunityDto> listByStatus(String status) {
        if (status == null || status.isBlank()) {
            return listAll();
        }
        return opportunityRepository.findByStatusIgnoreCase(status)
                .stream()
                .map(OpportunityDto::new)
                .collect(Collectors.toList());
    }

    public List<OpportunityDto> listSuggestions() {
        return opportunityRepository.findAll().stream()
                .map(OpportunityDto::new)
                .collect(Collectors.toList());
    }

    // List by ID
    public Optional<OpportunityDto> listById(Integer id) {
        return opportunityRepository.findById(id)
                .map(OpportunityDto::new);
    }

    // Create a new opportunity
    @Transactional
    public Opportunity create(OpportunityDto dto) {
        Client client = clientRepository.findById(dto.getClientId())
                .orElseThrow(() -> new RuntimeException("Client not found"));

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Quote quote = null;
        if (dto.getQuoteId() != null) {
            quote = quoteRepository.findById(dto.getQuoteId())
                    .orElseThrow(() -> new RuntimeException("Quote not found"));
        }

        Opportunity opportunity = new Opportunity();
        opportunity.setClient(client);
        opportunity.setUser(user);
        opportunity.setQuote(quote);
        opportunity.setStatus(dto.getStatus());
        opportunity.setProbability(dto.getProbability() != null ? dto.getProbability() : BigDecimal.ZERO);
        opportunity.setEstimatedValue(dto.getEstimatedValue());
        opportunity.setExpectedCloseDate(dto.getExpectedCloseDate());
        opportunity.setCreatedAt(LocalDateTime.now());
        opportunity.setUpdatedAt(LocalDateTime.now());

        return opportunityRepository.save(opportunity);
    }

    // Update an existing opportunity
    @Transactional
    public Opportunity update(Integer id, OpportunityDto dto) {
        Opportunity opportunity = opportunityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Opportunity not found"));

        if (dto.getClientId() != null) {
            Client client = clientRepository.findById(dto.getClientId())
                    .orElseThrow(() -> new RuntimeException("Client not found"));
            opportunity.setClient(client);
        }

        if (dto.getUserId() != null) {
            User user = userRepository.findById(dto.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            opportunity.setUser(user);
        }

        if (dto.getQuoteId() != null) {
            Quote quote = quoteRepository.findById(dto.getQuoteId())
                    .orElseThrow(() -> new RuntimeException("Quote not found"));
            opportunity.setQuote(quote);
        }

        if (dto.getStatus() != null) {
            opportunity.setStatus(dto.getStatus());
        }

        if (dto.getProbability() != null) {
            opportunity.setProbability(dto.getProbability());
        }

        if (dto.getEstimatedValue() != null) {
            opportunity.setEstimatedValue(dto.getEstimatedValue());
        }

        if (dto.getExpectedCloseDate() != null) {
            opportunity.setExpectedCloseDate(dto.getExpectedCloseDate());
        }

        opportunity.setUpdatedAt(LocalDateTime.now());

        return opportunityRepository.save(opportunity);
    }

    // Change status only (similar a actualizar status de quote)
    @Transactional
    public void updateStatus(Integer id, String newStatus) {
        Opportunity opportunity = opportunityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Opportunity not found"));
        opportunity.setStatus(newStatus);
        opportunity.setUpdatedAt(LocalDateTime.now());
        opportunityRepository.save(opportunity);
    }

    // Deactivate opportunity (soft delete)
    @Transactional
    public void deactivate(Integer id) {
        Opportunity opportunity = opportunityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Opportunity not found"));
        opportunity.setStatus("Eliminated");
        opportunity.setUpdatedAt(LocalDateTime.now());
        opportunityRepository.save(opportunity);
    }
}
