package org.wms.dto.report;

import java.math.BigDecimal;

/**
 * Represents aggregated metrics for sales during a period.
 *
 * @param totalRevenue  sum of sale totals
 * @param totalOrders   number of sales
 * @param averageTicket average value per sale
 * @param totalItems    units sold
 * @param totalDiscounts discounts applied to all items
 */
public record SalesSummaryDto(
        BigDecimal totalRevenue,
        Long totalOrders,
        BigDecimal averageTicket,
        Long totalItems,
        BigDecimal totalDiscounts
) {
}
