package org.wms.service.sale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wms.dto.sale.InvoiceDto;
import org.wms.model.sale.Sale;
import org.wms.model.sale.Invoice;
import org.wms.repository.sale.InvoiceRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class InvoiceService {

    @Autowired
    private InvoiceRepository invoiceRepository;


    public List<InvoiceDto> listAll() {
        return invoiceRepository.findAll()
                .stream()
                .map(InvoiceDto::new)
                .toList();
    }

    public Optional<InvoiceDto> listById(Integer id) {
        return invoiceRepository.findById(id).map(InvoiceDto::new);
    }


    public Invoice createManual(InvoiceDto dto, Sale sale) {
        Invoice invoice = new Invoice();
        invoice.setInvoiceSeries(dto.getInvoiceSeries());
        invoice.setInvoiceNumber(dto.getInvoiceNumber());
        invoice.setInvoiceDate(dto.getInvoiceDate());
        invoice.setTotalAmount(dto.getTotalAmount());
        invoice.setStatus(true);
        invoice.setCreatedAt(LocalDateTime.now());
        invoice.setUpdatedAt(LocalDateTime.now());
        invoice.setSale(sale);
        return invoiceRepository.save(invoice);
    }


    public Invoice createInvoiceFromSale(Sale sale) {
        Invoice invoice = new Invoice();


        invoice.setInvoiceSeries("A001");


        invoice.setInvoiceNumber(generateInvoiceNumber());


        invoice.setInvoiceDate(LocalDateTime.now());
        invoice.setTotalAmount(
                sale.getTotal() != null ? sale.getTotal() : BigDecimal.ZERO
        );
        invoice.setStatus(true);

        invoice.setCreatedAt(LocalDateTime.now());
        invoice.setUpdatedAt(LocalDateTime.now());
        invoice.setSale(sale);

        return invoiceRepository.save(invoice);
    }


    private String generateInvoiceNumber() {
        long count = invoiceRepository.count() + 1;
        return String.format("%05d", count);
    }


    public Invoice update(Integer idInvoice, InvoiceDto dto) {
        Invoice invoice = invoiceRepository.findById(idInvoice)
                .orElseThrow(() -> new RuntimeException("Invoice not found"));

        invoice.setInvoiceSeries(dto.getInvoiceSeries());
        invoice.setInvoiceNumber(dto.getInvoiceNumber());
        invoice.setInvoiceDate(dto.getInvoiceDate());
        invoice.setTotalAmount(dto.getTotalAmount());
        if (dto.getStatus() != null) {
            invoice.setStatus(dto.getStatus());
        }
        invoice.setUpdatedAt(LocalDateTime.now());

        return invoiceRepository.save(invoice);
    }


    public void deactivate(Integer idInvoice) {
        Invoice invoice = invoiceRepository.findById(idInvoice)
                .orElseThrow(() -> new RuntimeException("Invoice not found"));
        invoice.setStatus(false);
        invoice.setUpdatedAt(LocalDateTime.now());
        invoiceRepository.save(invoice);
    }
}
