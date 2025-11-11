package org.wms.dto.report;

import java.math.BigDecimal;

/**
 * Entry describing a top client by revenue.
 */
public record ClientTopDto(
        Integer clientId,
        String clientName,
        BigDecimal totalRevenue,
        Long totalOrders,
        BigDecimal averageTicket
) {
}
