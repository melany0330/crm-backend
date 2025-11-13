package org.wms.dto.report;

import java.math.BigDecimal;

/**
 * Represents an entity (product, client, etc.) used in top lists.
 *
 * @param label   name displayed in the UI
 * @param amount  accumulated revenue
 * @param quantity complementary metric (orders or units)
 */
public record SalesReportItemDto(
        String label,
        BigDecimal amount,
        Long quantity
) {
}
