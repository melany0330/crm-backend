package org.wms.service.sale;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wms.dto.sale.SaleBillDto;
import org.wms.dto.sale.SaleDetailDto;
import org.wms.dto.sale.SaleDto;
import org.wms.model.client.Client;
import org.wms.model.inventory.Inventory;
import org.wms.model.movements.Product;
import org.wms.model.movements.Transaction;
import org.wms.model.movements.TypeMovement;
import org.wms.model.sale.SaleDetail;
import org.wms.model.sale.Invoice;
import org.wms.model.sale.Sale;
import org.wms.repository.discount.DiscountRepository;
import org.wms.repository.inventory.InventoryRepository;
import org.wms.repository.movements.ProductRepository;
import org.wms.repository.movements.TransactionRepository;
import org.wms.repository.movements.TypeMovementRepository;
import org.wms.repository.sale.SaleRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SaleService {

    @Autowired
    private SaleRepository saleRepository;

    @Autowired
    private org.wms.repository.client.ClientRepository clientRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private DiscountRepository discountRepository;

    @Autowired
    private org.wms.repository.sale.SaleDetailRepository saleDetailRepository;

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private TypeMovementRepository typeMovementRepository;

    @Autowired
    private InvoiceService invoiceService;

    public List<SaleDto> listAll() {
        return saleRepository.findAll()
                .stream()
                .filter(Sale::getStatus)
                .map(SaleDto::new)
                .toList();
    }

    public Optional<SaleDto> listById(Long idSale) {
        return saleRepository.findById(idSale).map(SaleDto::new);
    }

    @Transactional
    public SaleBillDto create(SaleDto dto) {
        Client client = clientRepository.findById(dto.getIdClient())
                .orElseThrow(() -> new RuntimeException("Client not found"));

        Sale newSale = new Sale();
        newSale.setSaleDate(dto.getSaleDate());
        newSale.setStatus(true);
        newSale.setCreatedAt(LocalDateTime.now());
        newSale.setUpdatedAt(LocalDateTime.now());
        newSale.setClient(client);

        List<SaleDetail> details = new ArrayList<>();
        List<Transaction> allTransactions = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;


        TypeMovement salidaType = typeMovementRepository.findAll()
                .stream()
                .filter(tm -> "SALIDA".equalsIgnoreCase(tm.getName()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Tipo de movimiento SALIDA no encontrado"));

        for (SaleDetailDto detailDto : dto.getDetails()) {
            Product product = productRepository.findById(detailDto.getIdProduct())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            Integer remainingAmount = detailDto.getAmount();
            BigDecimal unitPrice = detailDto.getUnitPrice();

            BigDecimal discountAmount = BigDecimal.ZERO;
            var optionalDiscount = discountRepository.findBestActiveDiscount(product, remainingAmount);

            if (optionalDiscount.isPresent()) {
                var discount = optionalDiscount.get();
                BigDecimal percentage = discount.getPorcentaje().divide(BigDecimal.valueOf(100));
                discountAmount = unitPrice.multiply(BigDecimal.valueOf(remainingAmount)).multiply(percentage);
            }


            List<Inventory> inventoryList = inventoryRepository.findByProductAndStatusOrderByExpirationDateAsc(product, true);

            for (Inventory inventory : inventoryList) {
                if (remainingAmount <= 0) break;

                int availableQty = inventory.getCurrentQuantity();
                if (availableQty <= 0) continue;

                int toDeduct = Math.min(remainingAmount, availableQty);
                inventory.setCurrentQuantity(availableQty - toDeduct);
                inventory.setUpdatedAt(new Date());


                Transaction movement = new Transaction();
                movement.setInventory(inventory);
                movement.setAmount(toDeduct);
                movement.setMovementDate(new Date());
                movement.setReason("Salida por venta");
                movement.setUser(null);
                movement.setTypeMovement(salidaType);
                movement.setSale(newSale);
                movement.setPurchase(null);
                movement.setStatus(true);
                movement.setCreatedAt(new Date());
                movement.setUpdatedAt(new Date());

                allTransactions.add(movement);

                remainingAmount -= toDeduct;
            }

            if (remainingAmount > 0) {
                throw new RuntimeException("No hay suficiente inventario para el producto: " + product.getName());
            }

            SaleDetail detail = new SaleDetail();
            detail.setSale(newSale);
            detail.setProduct(product);
            detail.setAmount(detailDto.getAmount());
            detail.setUnitPrice(unitPrice);
            detail.setDiscount(discountAmount);
            detail.setStatus(true);
            detail.setCreatedAt(LocalDateTime.now());
            detail.setUpdatedAt(LocalDateTime.now());

            total = total.add(unitPrice.multiply(BigDecimal.valueOf(detailDto.getAmount())).subtract(discountAmount));
            details.add(detail);
        }

        newSale.setTotal(total);
        Sale savedSale = saleRepository.save(newSale);
        saleDetailRepository.saveAll(details);
        savedSale.setDetails(details);


        transactionRepository.saveAll(allTransactions);

        Invoice invoice = invoiceService.createInvoiceFromSale(savedSale);

        return new SaleBillDto(savedSale, invoice);
    }

    public void deactivate(Long idSale) {
        Sale sale = saleRepository.findById(idSale)
                .orElseThrow(() -> new RuntimeException("Sale not found"));
        sale.setStatus(false);
        sale.setUpdatedAt(LocalDateTime.now());

        saleRepository.save(sale);
    }

    public List<SaleDto> findSalesByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        List<Sale> sales = saleRepository.findBySaleDateBetween(startDate, endDate)
                .stream()
                .filter(Sale::getStatus)
                .collect(Collectors.toList());

        return sales.stream()
                .map(SaleDto::new)
                .collect(Collectors.toList());
    }
}
