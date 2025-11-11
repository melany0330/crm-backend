package org.wms.dto.report;

import java.util.List;

/**
 * Wraps all pieces of the sales report.
 *
 * @param summary     aggregated metrics
 * @param trend       revenue/orders per day
 * @param topProducts ranking of best-selling products
 * @param topClients  ranking of best clients by revenue
 */
public record SalesReportResponse(
        SalesSummaryDto summary,
        List<SalesTrendPointDto> trend,
        List<SalesReportItemDto> topProducts,
        List<SalesReportItemDto> topClients
) {
}
