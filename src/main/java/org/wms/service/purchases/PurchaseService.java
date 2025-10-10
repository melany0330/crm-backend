package org.wms.service.purchases;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.wms.dto.purchases.*;
import org.wms.model.auth.User;
import org.wms.model.inventory.Inventory;
import org.wms.model.movements.Product;
import org.wms.model.movements.Transaction;
import org.wms.model.movements.TypeMovement;
import org.wms.model.purchases.Provider;
import org.wms.model.purchases.Purchase;
import org.wms.model.purchases.PurchaseBill;
import org.wms.model.purchases.PurchaseDetail;
import org.wms.repository.auth.UserRepository;
import org.wms.repository.inventory.InventoryRepository;
import org.wms.repository.movements.ProductRepository;
import org.wms.repository.movements.TransactionRepository;
import org.wms.repository.purchases.ProviderRepository;
import org.wms.repository.purchases.PurchaseBillRepository;
import org.wms.repository.purchases.PurchaseDetailRepository;
import org.wms.repository.purchases.PurchaseRepository;

import javax.swing.text.html.Option;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;



@Service
public class PurchaseService {


    @Autowired
    private PurchaseRepository purchaseRepository;
    @Autowired
    private ProviderRepository providerRepository;
    @Autowired
    private PurchaseDetailRepository purchaseDetailRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private PurchaseBillRepository purchaseBillRepository;
    @Autowired
    private InventoryRepository inventoryRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TransactionRepository transactionRepository;


    @Transactional
    public List<PurchaseBillDto> listAll() {
        return purchaseBillRepository.findAll()
                .stream()
                .filter(bill -> bill.getPurchase() != null && bill.getPurchase().getStatus())
                .map(PurchaseBillDto::new)
                .toList();
    }
    @Transactional
    public Optional<PurchaseBillDto> listById(Integer idPurchase){
        return purchaseBillRepository.findById(idPurchase).map(PurchaseBillDto::new);
    }

    @Transactional
    public List<PurchaseBillDto> getReportByDateRange(LocalDateTime startDate, LocalDateTime endDate){
        List<PurchaseBill> bills = purchaseBillRepository.findByIssueDateBetween(startDate, endDate);
        return bills.stream()
                .sorted(Comparator.comparing(PurchaseBill::getIssueDate))
                .map(PurchaseBillDto::new)
                .toList();
    }

    @Transactional
    public PurchaseBillDto create(PurchaseRequestDto dto){
        Provider provider = providerRepository.findById(dto.getIdProvider())
                .orElseThrow(()->new RuntimeException("Provider not found"));

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUserName(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        BigDecimal calculatedTotal = dto.getDetails().stream()
                .map(detail -> {
                    Product product = productRepository.findById(detail.getIdProduct())
                            .orElseThrow(() -> new RuntimeException("Product not found"));
                    return product.getPurchasePrice().multiply(BigDecimal.valueOf(detail.getAmount()));
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (dto.getTotalAmount().compareTo(calculatedTotal) != 0) {
            throw new RuntimeException(("The total and the details don't match"));
        }

        Purchase newPurchase = new Purchase();
        newPurchase.setPurchaseDate(dto.getPurchaseDate());
        newPurchase.setTotalAmount(dto.getTotalAmount());
        newPurchase.setProvider(provider);
        newPurchase.setStatus(true);
        newPurchase.setCreatedAt(LocalDateTime.now());
        newPurchase.setUpdatedAt(LocalDateTime.now());



        Purchase savedPurchase = purchaseRepository.save(newPurchase);

        List<PurchaseDetail> details = new ArrayList<>();
        for (PurchaseDetailRequestDto detailDto : dto.getDetails()){
            PurchaseDetail detail = new PurchaseDetail();
            detail.setPurchase(savedPurchase);

            Product product = productRepository.findById(detailDto.getIdProduct())
                            .orElseThrow(()-> new RuntimeException("Product not found"));

            LocalDate today = LocalDate.now();
            String datePrefix = today.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            Long countToday = inventoryRepository.countByDate(today);
            String batchNumber = String.format("LOT-%s-%03d", datePrefix, countToday + 1);

            Inventory newInventory = new Inventory();
            newInventory.setProduct(product);
            newInventory.setBatch(batchNumber);
            newInventory.setExpirationDate(detailDto.getExpirationDate());
            newInventory.setCurrentQuantity(detailDto.getAmount());
            newInventory.setStatus(true);
            Inventory savedInventory = inventoryRepository.save(newInventory);

            Transaction newtransaction = new Transaction();
            newtransaction.setInventory(savedInventory);
            newtransaction.setAmount(detailDto.getAmount());
            newtransaction.setMovementDate(dto.getMovementDate());
            newtransaction.setReason(dto.getReason());
            newtransaction.setUser(user);
            newtransaction.setTypeMovement(new TypeMovement(2L));
            newtransaction.setPurchase(savedPurchase);
            newtransaction.setStatus(true);
            transactionRepository.save(newtransaction);

            detail.setProduct(product);
            detail.setAmount(detailDto.getAmount());
            detail.setUnitPrice(product.getPurchasePrice());
            detail.setCreatedAt(LocalDateTime.now());
            detail.setUpdatedAt(LocalDateTime.now());
            details.add(detail);
        }
        purchaseDetailRepository.saveAll(details);
        savedPurchase.setDetails(details);

        PurchaseBill bill = new PurchaseBill();
        bill.setSeries(dto.getSeries());
        bill.setBillNumber(dto.getBillNumber());
        bill.setIssueDate(dto.getIssueDate());
        bill.setBillTotal(savedPurchase.getTotalAmount());
        bill.setStatus(true);
        bill.setCreatedAt(LocalDateTime.now());
        bill.setUpdatedAt(LocalDateTime.now());
        bill.setPurchase(savedPurchase);
        PurchaseBill savedBill = purchaseBillRepository.save(bill);


        return new PurchaseBillDto(savedBill);
    }

    public void deactivate(Integer idPurchase) {
        Purchase deactivatedPurchase = purchaseRepository.findById(idPurchase)
                .orElseThrow(()-> new RuntimeException("Purchase not found"));
        deactivatedPurchase.setStatus(false);
        deactivatedPurchase.setUpdatedAt(java.time.LocalDateTime.now());

        purchaseRepository.save(deactivatedPurchase);
    }
      

}
