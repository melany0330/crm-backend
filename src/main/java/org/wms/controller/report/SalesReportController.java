package org.wms.controller.report;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import org.wms.dto.report.ClientInsightsResponse;
import org.wms.dto.report.ClientReportFilter;
import org.wms.dto.report.ReportExportFormat;
import org.wms.dto.report.SalesReportFilter;
import org.wms.dto.report.SalesReportResponse;
import org.wms.service.reports.ClientInsightsReportService;
import org.wms.service.reports.SalesReportService;
import org.wms.util.ApiResponse;

@Tag(name = "Reports", description = "Endpoints for sales and performance reports")
@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
@Validated
public class SalesReportController {

    private final SalesReportService salesReportService;
    private final ClientInsightsReportService clientInsightsReportService;

    @Operation(
            summary = "Sales summary and trends",
            description = "Generates basic sales KPIs, trends and top entities for the selected period"
    )
    @GetMapping("/sales")
    public ResponseEntity<ApiResponse<SalesReportResponse>> getSalesReport(
            @ModelAttribute SalesReportFilter filter
    ) {
        SalesReportResponse response = salesReportService.buildReport(filter);
        return ResponseEntity.ok(new ApiResponse<>("Sales report generated", response));
    }

    @Operation(
            summary = "Export sales report",
            description = "Exports the sales report as a CSV file"
    )
    @GetMapping("/sales/export")
    public ResponseEntity<byte[]> exportSalesReport(
            @ModelAttribute SalesReportFilter filter,
            @RequestParam(defaultValue = "csv") String format
    ) {
        validateFormat(format);
        byte[] payload = salesReportService.exportCsv(filter);
        return buildAttachment(payload, "sales-report.csv");
    }

    @Operation(
            summary = "Client insights",
            description = "Generates KPIs for the most important clients and purchased products"
    )
    @GetMapping("/clients")
    public ResponseEntity<ApiResponse<ClientInsightsResponse>> getClientInsights(
            @ModelAttribute ClientReportFilter filter
    ) {
        ClientInsightsResponse response = clientInsightsReportService.buildReport(filter);
        return ResponseEntity.ok(new ApiResponse<>("Client insights generated", response));
    }

    @Operation(
            summary = "Export client insights",
            description = "Exports the client insights report"
    )
    @GetMapping("/clients/export")
    public ResponseEntity<byte[]> exportClientInsights(
            @ModelAttribute ClientReportFilter filter,
            @RequestParam(defaultValue = "csv") String format
    ) {
        validateFormat(format);
        byte[] payload = clientInsightsReportService.exportCsv(filter);
        return buildAttachment(payload, "client-insights.csv");
    }

    private void validateFormat(String format) {
        ReportExportFormat chosen = ReportExportFormat.fromString(format);
        if (chosen != ReportExportFormat.CSV) {
            throw new IllegalArgumentException("Only CSV export is supported at the moment");
        }
    }

    private ResponseEntity<byte[]> buildAttachment(byte[] payload, String filename) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("text/csv"));
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename);
        return ResponseEntity.ok()
                .headers(headers)
                .body(payload);
    }
}
