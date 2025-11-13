package org.wms.dto.report;

import java.math.BigDecimal;

/**
 * Product popularity metrics.
 */
public record ClientProductStatDto(
        Integer productId,
        String productName,
        Long quantity,
        BigDecimal revenue
) {
}
