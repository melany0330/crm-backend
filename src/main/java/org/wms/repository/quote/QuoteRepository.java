package org.wms.repository.quote;

import org.springframework.data.jpa.repository.JpaRepository;
import org.wms.model.quote.Quote;

import java.time.LocalDateTime;
import java.util.List;

public interface QuoteRepository extends JpaRepository<Quote, Integer> {

    // ⬇️ NUEVO: adapta el nombre del campo según tu entidad Client (idClient o idCliente)
    List<Quote> findByClient_IdClient(Integer idClient);
    // Si tu Client tuviera getIdCliente():
    // List<Quote> findByClient_IdCliente(Integer idCliente);

    List<Quote> findByQuoteDateBetween(LocalDateTime start, LocalDateTime end);
}
