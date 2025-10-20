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

    @Autowired
    private QuoteRepository quoteRepository;

    @Autowired
    private QuoteDetailRepository quoteDetailRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    /**
     * Listar todas las cotizaciones activas
     */
    public List<QuoteDto> listAll() {
        return quoteRepository.findAll()
                .stream()
                .filter(quote -> !"Eliminada".equalsIgnoreCase(quote.getStatus()))
                .map(QuoteDto::new)
                .toList();
    }

    /**
     * Buscar cotización por ID
     */
    public Optional<QuoteDto> listById(Integer idQuote) {
        return quoteRepository.findById(idQuote).map(QuoteDto::new);
    }

    /**
     * Crear nueva cotización
     */
    @Transactional
    public QuoteDto create(QuoteDto dto) {
        Client client = clientRepository.findById(dto.getIdClient())
                .orElseThrow(() -> new RuntimeException("Client not found"));

        // 🔹 Manejar user opcionalmente
        User user = null;
        if (dto.getIdUser() != null) {
            user = userRepository.findById(dto.getIdUser())
                    .orElseThrow(() -> new RuntimeException("User not found"));
        }

        Quote newQuote = new Quote();
        newQuote.setClient(client);
        newQuote.setUser(user); // puede ser null
        newQuote.setQuoteDate(dto.getQuoteDate());
        newQuote.setStatus("Pendiente");
        newQuote.setCreatedAt(LocalDateTime.now());
        newQuote.setUpdatedAt(LocalDateTime.now());

        List<QuoteDetail> details = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        for (QuoteDetailDto detailDto : dto.getDetails()) {
            Product product = productRepository.findById(detailDto.getIdProduct())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            QuoteDetail detail = new QuoteDetail();
            detail.setQuote(newQuote);
            detail.setProduct(product);
            detail.setQuantity(detailDto.getQuantity());
            detail.setUnitPrice(detailDto.getUnitPrice());
            detail.setDiscount(detailDto.getDiscount());

            BigDecimal subtotal = detailDto.getUnitPrice()
                    .multiply(BigDecimal.valueOf(detailDto.getQuantity()))
                    .subtract(detailDto.getDiscount() != null ? detailDto.getDiscount() : BigDecimal.ZERO);

            total = total.add(subtotal);
            details.add(detail);
        }

        newQuote.setTotal(total);
        Quote savedQuote = quoteRepository.save(newQuote);
        quoteDetailRepository.saveAll(details);

        savedQuote.setDetails(details);
        return new QuoteDto(savedQuote);
    }

    /**
     * Cambiar estado de cotización (ej: Pendiente, Aceptada, Rechazada)
     */
    public void updateStatus(Integer idQuote, String newStatus) {
        Quote quote = quoteRepository.findById(idQuote)
                .orElseThrow(() -> new RuntimeException("Quote not found"));

        quote.setStatus(newStatus);
        quote.setUpdatedAt(LocalDateTime.now());
        quoteRepository.save(quote);
    }

    /**
     * Eliminar (desactivar) cotización
     */
    public void deactivate(Integer idQuote) {
        Quote quote = quoteRepository.findById(idQuote)
                .orElseThrow(() -> new RuntimeException("Quote not found"));

        quote.setStatus("Eliminada");
        quote.setUpdatedAt(LocalDateTime.now());
        quoteRepository.save(quote);
    }
}
