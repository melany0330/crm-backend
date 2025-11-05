package org.wms.service.quote;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wms.dto.quote.QuoteDetailDto;
import org.wms.dto.quote.QuoteDto;
import org.wms.model.client.Client;
import org.wms.model.quote.Quote;
import org.wms.model.quote.QuoteDetail;
import org.wms.model.movements.Product;
import org.wms.model.auth.User;
import org.wms.repository.client.ClientRepository;
import org.wms.repository.quote.QuoteDetailRepository;
import org.wms.repository.quote.QuoteRepository;
import org.wms.repository.movements.ProductRepository;
import org.wms.repository.auth.UserRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class QuoteService {

    @Autowired private QuoteRepository quoteRepository;
    @Autowired private QuoteDetailRepository quoteDetailRepository;
    @Autowired private ClientRepository clientRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private ProductRepository productRepository;

    /** Listar todas las cotizaciones activas */
    public List<QuoteDto> listAll() {
        return quoteRepository.findAll()
                .stream()
                .filter(q -> !"Eliminada".equalsIgnoreCase(q.getStatus()))
                .map(QuoteDto::new)
                .toList();
    }

    /** ⬇️ NUEVO: listar por cliente */
    public List<QuoteDto> listByClient(Integer idClient) {
        return quoteRepository.findByClient_IdClient(idClient)
                .stream()
                .filter(q -> !"Eliminada".equalsIgnoreCase(q.getStatus()))
                .map(QuoteDto::new)
                .toList();
    }

    /** Buscar por ID */
    public Optional<QuoteDto> listById(Integer idQuote) {
        return quoteRepository.findById(idQuote).map(QuoteDto::new);
    }

    /** Crear */
    @Transactional
    public QuoteDto create(QuoteDto dto) {
        Client client = clientRepository.findById(dto.getIdClient())
                .orElseThrow(() -> new RuntimeException("Client not found"));

        User user = null;
        if (dto.getIdUser() != null) {
            user = userRepository.findById(dto.getIdUser())
                    .orElseThrow(() -> new RuntimeException("User not found"));
        }

        Quote newQuote = new Quote();
        newQuote.setClient(client);
        newQuote.setUser(user);
        newQuote.setQuoteDate(dto.getQuoteDate());
        newQuote.setStatus("Pendiente");
        newQuote.setCreatedAt(LocalDateTime.now());
        newQuote.setUpdatedAt(LocalDateTime.now());

        List<QuoteDetail> details = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        for (QuoteDetailDto d : dto.getDetails()) {
            Product product = productRepository.findById(d.getIdProduct())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            int qty = d.getQuantity() != null ? d.getQuantity() : 0;
            BigDecimal unit = d.getUnitPrice() != null ? d.getUnitPrice() : BigDecimal.ZERO;
            BigDecimal disc = d.getDiscount() != null ? d.getDiscount() : BigDecimal.ZERO;

            QuoteDetail detail = new QuoteDetail();
            detail.setQuote(newQuote);
            detail.setProduct(product);
            detail.setQuantity(qty);
            detail.setUnitPrice(unit);
            detail.setDiscount(disc);

            BigDecimal subtotal = unit.multiply(BigDecimal.valueOf(qty)).subtract(disc);
            total = total.add(subtotal);

            details.add(detail);
        }

        newQuote.setTotal(total);
        Quote saved = quoteRepository.save(newQuote);
        quoteDetailRepository.saveAll(details);
        saved.setDetails(details);

        return new QuoteDto(saved);
    }

    /** Cambiar estado */
    public void updateStatus(Integer idQuote, String newStatus) {
        Quote quote = quoteRepository.findById(idQuote)
                .orElseThrow(() -> new RuntimeException("Quote not found"));
        quote.setStatus(newStatus);
        quote.setUpdatedAt(LocalDateTime.now());
        quoteRepository.save(quote);
    }

    /** Eliminar (marcar Eliminada) */
    public void deactivate(Integer idQuote) {
        Quote quote = quoteRepository.findById(idQuote)
                .orElseThrow(() -> new RuntimeException("Quote not found"));
        quote.setStatus("Eliminada");
        quote.setUpdatedAt(LocalDateTime.now());
        quoteRepository.save(quote);
    }
}
