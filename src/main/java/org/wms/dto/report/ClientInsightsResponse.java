package org.wms.dto.report;

import java.util.List;

public record ClientInsightsResponse(
        ClientInsightsSummaryDto summary,
        List<ClientTopDto> topClients,
        List<ClientProductStatDto> topProducts,
        List<SalesTrendPointDto> trend
) {
}
