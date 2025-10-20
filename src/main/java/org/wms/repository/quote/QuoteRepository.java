package org.wms.repository.quote;

import org.springframework.data.jpa.repository.JpaRepository;
import org.wms.model.quote.Quote;

public interface QuoteRepository extends JpaRepository<Quote, Integer> {
}