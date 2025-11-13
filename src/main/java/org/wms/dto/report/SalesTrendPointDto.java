package org.wms.dto.report;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Represents aggregated revenue/orders for a given day.
 *
 * @param date    day bucket
 * @param revenue amount sold on that day
 * @param orders  number of orders
 */
public record SalesTrendPointDto(
        LocalDate date,
        BigDecimal revenue,
        Long orders
) {
}
