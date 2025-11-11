package org.wms.service.reports;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import org.wms.dto.report.ClientInsightsResponse;
import org.wms.dto.report.ClientInsightsSummaryDto;
import org.wms.dto.report.ClientProductStatDto;
import org.wms.dto.report.ClientReportFilter;
import org.wms.dto.report.ClientTopDto;
import org.wms.dto.report.SalesTrendPointDto;
import org.wms.model.client.Client;
import org.wms.model.movements.Product;
import org.wms.model.sale.Sale;
import org.wms.model.sale.SaleDetail;
import org.wms.repository.sale.SaleRepository;

@Service
@RequiredArgsConstructor
public class ClientInsightsReportService {

    private static final int DEFAULT_RANGE_DAYS = 30;

    private final SaleRepository saleRepository;

    public ClientInsightsResponse buildReport(ClientReportFilter filter) {
        ClientFilters resolved = resolveFilter(filter);
        List<Sale> sales = saleRepository.findBySaleDateBetween(resolved.from(), resolved.to())
                .stream()
                .filter(Sale::getStatus)
                .filter(sale -> resolved.clientId() == null || matchesClient(resolved.clientId(), sale.getClient()))
                .toList();

        ClientInsightsSummaryDto summary = summarize(sales);
        List<ClientTopDto> topClients = buildTopClients(sales);
        List<ClientProductStatDto> topProducts = buildTopProducts(sales, resolved.categoryId());
        List<SalesTrendPointDto> trend = buildTrend(sales);

        return new ClientInsightsResponse(summary, topClients, topProducts, trend);
    }

    public byte[] exportCsv(ClientReportFilter filter) {
        ClientInsightsResponse response = buildReport(filter);
        StringBuilder csv = new StringBuilder();
        csv.append("Metric,Value\n");
        ClientInsightsSummaryDto summary = response.summary();
        csv.append("Total Revenue,").append(summary.totalRevenue()).append("\n");
        csv.append("Total Clients,").append(summary.totalClients()).append("\n");
        csv.append("Total Orders,").append(summary.totalOrders()).append("\n");
        csv.append("Average Ticket,").append(summary.averageTicket()).append("\n\n");

        csv.append("Top Clients\n");
        csv.append("Client,Revenue,Orders,Average Ticket\n");
        response.topClients().forEach(c ->
                csv.append(safe(c.clientName())).append(",")
                        .append(c.totalRevenue()).append(",")
                        .append(c.totalOrders()).append(",")
                        .append(c.averageTicket()).append("\n")
        );

        csv.append("\nTop Products\n");
        csv.append("Product,Quantity,Revenue\n");
        response.topProducts().forEach(p ->
                csv.append(safe(p.productName())).append(",")
                        .append(p.quantity()).append(",")
                        .append(p.revenue()).append("\n")
        );

        csv.append("\nTrend\n");
        csv.append("Date,Revenue,Orders\n");
        response.trend().forEach(t ->
                csv.append(t.date()).append(",")
                        .append(t.revenue()).append(",")
                        .append(t.orders()).append("\n")
        );

        return csv.toString().getBytes();
    }

    private boolean matchesClient(Integer clientId, Client client) {
        if (client == null || client.getIdClient() == null) {
            return false;
        }
        return Objects.equals(clientId, client.getIdClient());
    }

    private ClientInsightsSummaryDto summarize(List<Sale> sales) {
        BigDecimal totalRevenue = sales.stream()
                .map(Sale::getTotal)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        long totalOrders = sales.size();
        long distinctClients = sales.stream()
                .map(Sale::getClient)
                .filter(Objects::nonNull)
                .map(Client::getIdClient)
                .filter(Objects::nonNull)
                .distinct()
                .count();

        BigDecimal averageTicket = totalOrders == 0
                ? BigDecimal.ZERO
                : totalRevenue.divide(BigDecimal.valueOf(totalOrders), 2, RoundingMode.HALF_UP);

        return new ClientInsightsSummaryDto(totalRevenue, distinctClients, totalOrders, averageTicket);
    }

    private List<ClientTopDto> buildTopClients(List<Sale> sales) {
        Map<Integer, ClientAggregate> aggregates = new HashMap<>();
        for (Sale sale : sales) {
            Client client = sale.getClient();
            if (client == null || client.getIdClient() == null) {
                continue;
            }
            ClientAggregate agg = aggregates.computeIfAbsent(
                    client.getIdClient(),
                    id -> new ClientAggregate(client.getIdClient(), buildClientLabel(client))
            );
            agg.addSale(sale);
        }

        return aggregates.values()
                .stream()
                .sorted((a, b) -> b.totalRevenue.compareTo(a.totalRevenue))
                .map(ClientAggregate::toDto)
                .collect(Collectors.toList());
    }

    private List<ClientProductStatDto> buildTopProducts(List<Sale> sales, Integer categoryFilter) {
        Map<Integer, ProductAggregate> aggregates = new HashMap<>();

        for (Sale sale : sales) {
            for (SaleDetail detail : safeDetails(sale)) {
                Product product = detail.getProduct();
                if (product == null || product.getIdProduct() == null) {
                    continue;
                }
                if (categoryFilter != null) {
                    Integer productCategory = product.getCategory() != null ? product.getCategory().getIdCategory() : null;
                    if (!Objects.equals(categoryFilter, productCategory)) {
                        continue;
                    }
                }
                ProductAggregate agg = aggregates.computeIfAbsent(
                        product.getIdProduct(),
                        id -> new ProductAggregate(product.getIdProduct(), product.getName())
                );
                agg.addDetail(detail);
            }
        }

        return aggregates.values()
                .stream()
                .sorted((a, b) -> b.revenue.compareTo(a.revenue))
                .map(ProductAggregate::toDto)
                .collect(Collectors.toList());
    }

    private List<SalesTrendPointDto> buildTrend(List<Sale> sales) {
        Map<LocalDate, List<Sale>> grouped = new LinkedHashMap<>();

        for (Sale sale : sales) {
            if (sale.getSaleDate() == null) {
                continue;
            }
            LocalDate date = sale.getSaleDate().toLocalDate();
            grouped.computeIfAbsent(date, key -> new ArrayList<>()).add(sale);
        }

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
                .collect(Collectors.toList());
    }

    private List<SaleDetail> safeDetails(Sale sale) {
        if (sale == null || sale.getDetails() == null) {
            return List.of();
        }
        return sale.getDetails()
                .stream()
                .filter(detail -> detail.getStatus() == null || detail.getStatus())
                .collect(Collectors.toList());
    }

    private ClientFilters resolveFilter(ClientReportFilter filter) {
        ClientReportFilter safe = filter != null ? filter : new ClientReportFilter();
        LocalDate end = safe.getEndDate() != null ? safe.getEndDate() : LocalDate.now();
        LocalDate start = safe.getStartDate() != null ? safe.getStartDate() : end.minusDays(DEFAULT_RANGE_DAYS);

        if (start.isAfter(end)) {
            LocalDate tmp = start;
            start = end;
            end = tmp;
        }

        LocalDateTime from = start.atStartOfDay();
        LocalDateTime to = end.atTime(LocalTime.MAX);

        return new ClientFilters(from, to, safe.getClientId(), safe.getCategoryId());
    }

    private String buildClientLabel(Client client) {
        List<String> pieces = new ArrayList<>();
        if (client.getFirstName() != null) {
            pieces.add(client.getFirstName());
        }
        if (client.getLastName() != null) {
            pieces.add(client.getLastName());
        }
        String label = String.join(" ", pieces).trim();
        if (label.isEmpty()) {
            return client.getEmail() != null ? client.getEmail() : "Cliente sin nombre";
        }
        return label;
    }

    private String safe(String text) {
        return text == null ? "" : text.replaceAll("[\\r\\n]+", " ").trim();
    }

    private record ClientFilters(LocalDateTime from, LocalDateTime to, Integer clientId, Integer categoryId) {
    }

    private static final class ClientAggregate {
        private final Integer clientId;
        private final String clientName;
        private BigDecimal totalRevenue = BigDecimal.ZERO;
        private long totalOrders;

        private ClientAggregate(Integer clientId, String clientName) {
            this.clientId = clientId;
            this.clientName = clientName;
        }

        private void addSale(Sale sale) {
            if (sale.getTotal() != null) {
                totalRevenue = totalRevenue.add(sale.getTotal());
            }
            totalOrders++;
        }

        private ClientTopDto toDto() {
            BigDecimal avg = totalOrders == 0
                    ? BigDecimal.ZERO
                    : totalRevenue.divide(BigDecimal.valueOf(totalOrders), 2, RoundingMode.HALF_UP);
            return new ClientTopDto(clientId, clientName, totalRevenue, totalOrders, avg);
        }
    }

    private static final class ProductAggregate {
        private final Integer productId;
        private final String productName;
        private long quantity = 0;
        private BigDecimal revenue = BigDecimal.ZERO;

        private ProductAggregate(Integer productId, String productName) {
            this.productId = productId;
            this.productName = productName;
        }

        private void addDetail(SaleDetail detail) {
            if (detail.getAmount() != null) {
                quantity += detail.getAmount();
            }
            if (detail.getUnitPrice() != null && detail.getAmount() != null) {
                BigDecimal gross = detail.getUnitPrice()
                        .multiply(BigDecimal.valueOf(detail.getAmount()));
                BigDecimal discount = detail.getDiscount() != null ? detail.getDiscount() : BigDecimal.ZERO;
                revenue = revenue.add(gross.subtract(discount));
            }
        }

        private ClientProductStatDto toDto() {
            return new ClientProductStatDto(productId, productName, quantity, revenue);
        }
    }
}
