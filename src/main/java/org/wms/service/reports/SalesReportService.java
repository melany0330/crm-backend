package org.wms.service.reports;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import org.wms.dto.report.SalesReportFilter;
import org.wms.dto.report.SalesReportItemDto;
import org.wms.dto.report.SalesReportResponse;
import org.wms.dto.report.SalesSummaryDto;
import org.wms.dto.report.SalesTrendPointDto;
import org.wms.model.client.Client;
import org.wms.model.movements.Product;
import org.wms.model.sale.Sale;
import org.wms.model.sale.SaleDetail;
import org.wms.repository.sale.SaleRepository;

@Service
@RequiredArgsConstructor
public class SalesReportService {

    private static final int DEFAULT_RANGE_DAYS = 30;

    private final SaleRepository saleRepository;

    public SalesReportResponse buildReport(SalesReportFilter filter) {
        SalesReportFilter safeFilter = filter != null ? filter : new SalesReportFilter();
        DateRange range = resolveRange(safeFilter);

        List<Sale> sales = saleRepository.findBySaleDateBetween(range.start(), range.end())
                .stream()
                .filter(Sale::getStatus)
                .filter(sale -> matchesClient(safeFilter.getClientId(), sale))
                .toList();

        SalesSummaryDto summary = summarize(sales);
        List<SalesTrendPointDto> trend = buildTrend(sales);
        List<SalesReportItemDto> topProducts = collectTopProducts(sales);
        List<SalesReportItemDto> topClients = collectTopClients(sales);

        return new SalesReportResponse(summary, trend, topProducts, topClients);
    }

    public byte[] exportCsv(SalesReportFilter filter) {
        SalesReportResponse response = buildReport(filter);
        StringBuilder csv = new StringBuilder();
        csv.append("Metric,Value\n");
        SalesSummaryDto summary = response.summary();
        csv.append("Total Revenue,").append(summary.totalRevenue()).append("\n");
        csv.append("Total Orders,").append(summary.totalOrders()).append("\n");
        csv.append("Average Ticket,").append(summary.averageTicket()).append("\n");
        csv.append("Total Items,").append(summary.totalItems()).append("\n");
        csv.append("Total Discounts,").append(summary.totalDiscounts()).append("\n\n");

        csv.append("Top Products\n");
        csv.append("Product,Revenue,Units\n");
        response.topProducts().forEach(item ->
                csv.append(safe(item.label())).append(",")
                        .append(item.amount()).append(",")
                        .append(item.quantity()).append("\n")
        );

        csv.append("\nTop Clients\n");
        csv.append("Client,Revenue,Orders\n");
        response.topClients().forEach(item ->
                csv.append(safe(item.label())).append(",")
                        .append(item.amount()).append(",")
                        .append(item.quantity()).append("\n")
        );

        csv.append("\nTrend\n");
        csv.append("Date,Revenue,Orders\n");
        response.trend().forEach(point ->
                csv.append(point.date()).append(",")
                        .append(point.revenue()).append(",")
                        .append(point.orders()).append("\n")
        );

        return csv.toString().getBytes();
    }

    private DateRange resolveRange(SalesReportFilter filter) {
        LocalDate start = filter.getStartDate();
        LocalDate end = filter.getEndDate();

        LocalDate computedEnd = end != null ? end : LocalDate.now();
        LocalDate computedStart = start != null ? start : computedEnd.minusDays(DEFAULT_RANGE_DAYS);

        if (computedStart.isAfter(computedEnd)) {
            LocalDate tmp = computedStart;
            computedStart = computedEnd;
            computedEnd = tmp;
        }

        LocalDateTime from = computedStart.atStartOfDay();
        LocalDateTime to = computedEnd.atTime(LocalTime.MAX);
        return new DateRange(from, to);
    }

    private boolean matchesClient(Integer clientId, Sale sale) {
        if (clientId == null) {
            return true;
        }
        if (sale.getClient() == null || sale.getClient().getIdClient() == null) {
            return false;
        }
        return Objects.equals(clientId, sale.getClient().getIdClient());
    }

    private SalesSummaryDto summarize(List<Sale> sales) {
        BigDecimal totalRevenue = sales.stream()
                .map(Sale::getTotal)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        long totalOrders = sales.size();

        List<SaleDetail> allDetails = sales.stream()
                .flatMap(sale -> safeDetails(sale).stream())
                .toList();

        long totalItems = allDetails.stream()
                .map(SaleDetail::getAmount)
                .filter(Objects::nonNull)
                .mapToLong(Integer::longValue)
                .sum();

        BigDecimal totalDiscounts = allDetails.stream()
                .map(SaleDetail::getDiscount)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal averageTicket = totalOrders == 0
                ? BigDecimal.ZERO
                : totalRevenue.divide(BigDecimal.valueOf(totalOrders), 2, RoundingMode.HALF_UP);

        return new SalesSummaryDto(totalRevenue, totalOrders, averageTicket, totalItems, totalDiscounts);
    }

    private List<SalesTrendPointDto> buildTrend(List<Sale> sales) {
        Map<LocalDate, List<Sale>> grouped = sales.stream()
                .filter(sale -> sale.getSaleDate() != null)
                .collect(Collectors.groupingBy(
                        sale -> sale.getSaleDate().toLocalDate(),
                        TreeMap::new,
                        Collectors.toList()
                ));

        return grouped.entrySet()
                .stream()
                .map(entry -> {
                    BigDecimal revenue = entry.getValue()
                            .stream()
                            .map(Sale::getTotal)
                            .filter(Objects::nonNull)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);
                    long orders = entry.getValue().size();
                    return new SalesTrendPointDto(entry.getKey(), revenue, orders);
                })
                .toList();
    }

    private List<SalesReportItemDto> collectTopProducts(List<Sale> sales) {
        Map<Integer, Aggregate> aggregates = new HashMap<>();

        for (Sale sale : sales) {
            for (SaleDetail detail : safeDetails(sale)) {
                Product product = detail.getProduct();
                if (product == null || product.getIdProduct() == null) {
                    continue;
                }
                BigDecimal netAmount = calculateNet(detail);
                long quantity = detail.getAmount() != null ? detail.getAmount() : 0;
                aggregates
                        .computeIfAbsent(product.getIdProduct(), id -> new Aggregate(product.getName()))
                        .accumulate(netAmount, quantity);
            }
        }

        return aggregates.values()
                .stream()
                .sorted(Comparator.comparing(Aggregate::amount).reversed())
                .limit(5)
                .map(agg -> new SalesReportItemDto(agg.label, agg.amount, agg.quantity))
                .toList();
    }

    private List<SalesReportItemDto> collectTopClients(List<Sale> sales) {
        Map<Integer, Aggregate> aggregates = new HashMap<>();

        for (Sale sale : sales) {
            Client client = sale.getClient();
            if (client == null || client.getIdClient() == null) {
                continue;
            }
            String label = buildClientLabel(client);
            BigDecimal amount = sale.getTotal() != null ? sale.getTotal() : BigDecimal.ZERO;
            aggregates
                    .computeIfAbsent(client.getIdClient(), id -> new Aggregate(label))
                    .accumulate(amount, 1);
        }

        return aggregates.values()
                .stream()
                .sorted(Comparator.comparing(Aggregate::amount).reversed())
                .limit(5)
                .map(agg -> new SalesReportItemDto(agg.label, agg.amount, agg.quantity))
                .toList();
    }

    private List<SaleDetail> safeDetails(Sale sale) {
        if (sale == null || sale.getDetails() == null) {
            return Collections.emptyList();
        }
        return sale.getDetails()
                .stream()
                .filter(detail -> detail.getStatus() == null || detail.getStatus())
                .collect(Collectors.toList());
    }

    private BigDecimal calculateNet(SaleDetail detail) {
        if (detail == null) {
            return BigDecimal.ZERO;
        }
        BigDecimal unitPrice = detail.getUnitPrice() != null ? detail.getUnitPrice() : BigDecimal.ZERO;
        long quantity = detail.getAmount() != null ? detail.getAmount() : 0;
        BigDecimal gross = unitPrice.multiply(BigDecimal.valueOf(quantity));
        BigDecimal discount = detail.getDiscount() != null ? detail.getDiscount() : BigDecimal.ZERO;
        return gross.subtract(discount);
    }

    private String buildClientLabel(Client client) {
        List<String> parts = new ArrayList<>();
        if (client.getFirstName() != null) {
            parts.add(client.getFirstName());
        }
        if (client.getLastName() != null) {
            parts.add(client.getLastName());
        }
        String label = String.join(" ", parts).trim();
        return label.isEmpty() ? client.getEmail() : label;
    }

    private record DateRange(LocalDateTime start, LocalDateTime end) {
    }

    private static final class Aggregate {
        private final String label;
        private BigDecimal amount = BigDecimal.ZERO;
        private long quantity;

        private Aggregate(String label) {
            this.label = label;
        }

        private void accumulate(BigDecimal delta, long deltaQuantity) {
            BigDecimal safeDelta = delta != null ? delta : BigDecimal.ZERO;
            this.amount = this.amount.add(safeDelta);
            this.quantity += deltaQuantity;
        }

        private BigDecimal amount() {
            return amount;
        }
    }

    private String safe(String value) {
        return value == null ? "" : value.replaceAll("[\\r\\n]+", " ").trim();
    }
}
