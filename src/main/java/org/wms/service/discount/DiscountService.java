package org.wms.service.discount;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wms.dto.discount.DiscountDto;
import org.wms.model.discount.Discount;
import org.wms.model.movements.Product;
import org.wms.repository.discount.DiscountRepository;
import org.wms.repository.movements.ProductRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DiscountService {

    @Autowired
    private DiscountRepository discountRepository;

    @Autowired
    private ProductRepository productRepository;

    public List<DiscountDto> listAll() {
        return discountRepository.findAll()
                .stream()
                .filter(Discount::getEstado)
                .map(DiscountDto::new)
                .collect(Collectors.toList());
    }
    public List<DiscountDto> listAllDiscounts() {
        return discountRepository.findAll()
                .stream()
                .map(DiscountDto::new)
                .collect(Collectors.toList());
    }

    public Optional<DiscountDto> listById(Integer id) {
        return discountRepository.findById(id)
                .map(DiscountDto::new);
    }

    @Transactional
    public Discount create(DiscountDto dto) {
        Product product = productRepository.findById(dto.getIdProducto())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Discount discount = new Discount();
        discount.setNombreDescuento(dto.getNombreDescuento());
        discount.setCantidadMin(dto.getCantidadMin());
        discount.setPorcentaje(dto.getPorcentaje());
        discount.setEstado(true);
        discount.setCreatedAt(LocalDateTime.now());
        discount.setUpdatedAt(LocalDateTime.now());
        discount.setProduct(product);

        return discountRepository.save(discount);
    }

    @Transactional
    public Discount update(Integer id, DiscountDto dto) {
        Discount discount = discountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Discount not found"));

        discount.setNombreDescuento(dto.getNombreDescuento());
        discount.setCantidadMin(dto.getCantidadMin());
        discount.setPorcentaje(dto.getPorcentaje());

        if (dto.getEstado() != null) {
            discount.setEstado(dto.getEstado());
        }
        discount.setUpdatedAt(LocalDateTime.now());

        if (dto.getIdProducto() != null) {
            Product product = productRepository.findById(dto.getIdProducto())
                    .orElseThrow(() -> new RuntimeException("Product not found"));
            discount.setProduct(product);
        }

        return discountRepository.save(discount);
    }

    @Transactional
    public void deactivate(Integer id) {
        Discount discount = discountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Discount not found"));
        discount.setEstado(false);
        discount.setUpdatedAt(LocalDateTime.now());
        discountRepository.save(discount);
    }

    public Optional<Discount> getBestDiscountForProduct(Product product, int cantidad) {
        return discountRepository.findBestActiveDiscount(product, cantidad);
    }

    public BigDecimal calcularPrecioConDescuento(Product product, int cantidad, BigDecimal precioUnitario) {
        Optional<Discount> optional = getBestDiscountForProduct(product, cantidad);
        if (optional.isPresent()) {
            BigDecimal porcentaje = optional.get().getPorcentaje();
            BigDecimal descuento = precioUnitario.multiply(porcentaje).divide(BigDecimal.valueOf(100));
            return precioUnitario.subtract(descuento);
        }
        return precioUnitario;
    }
}