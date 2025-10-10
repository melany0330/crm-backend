package org.wms.service.movements;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.wms.dto.movements.ProductDto;
import org.wms.dto.movements.ProductRequestDto;
import org.wms.model.movements.Category;
import org.wms.model.movements.Product;
import org.wms.repository.movements.CategoryRepository;
import org.wms.repository.movements.ProductRepository;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.nio.file.*;


@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    CategoryRepository categoryRepository;

    public List<ProductDto> listAll() {
        return productRepository.findAll()
                .stream().map(ProductDto::new)
                .toList();
    }
    public List<ProductDto> catalog() {
        return productRepository.findAll()
                .stream()
                .filter(Product::getStatus)
                .map(ProductDto::new)
                .toList();
    }

    public Optional<ProductDto> listById(Integer idProduct){
        return productRepository.findById(idProduct).map(ProductDto::new);
    }
    public Product create (ProductRequestDto dto, MultipartFile imageFile) {
        Category category = categoryRepository.findById(dto.getIdCategory())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        String fileName = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();
        String uploadDir = "/app/uploads/";
        Path uploadPath = Paths.get(uploadDir);

        try {
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            Path filePath = uploadPath.resolve(fileName);
            Files.copy(imageFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            throw new RuntimeException("Failed to store image");
        }

        Product newProduct = new Product();
        newProduct.setName(dto.getName());
        newProduct.setDescription(dto.getDescription());
        newProduct.setImage("/uploads/" + fileName);
        newProduct.setSalePrice(dto.getSalePrice());
        newProduct.setPurchasePrice(dto.getPurchasePrice());
        newProduct.setStatus(true);
        newProduct.setCategory(category);
        newProduct.setCreatedAt(LocalDateTime.now());
        newProduct.setUpdatedAt(LocalDateTime.now());

        return productRepository.save(newProduct);
    }
    public Product update(Integer idProduct, ProductRequestDto dto, MultipartFile imageFile) {
        Product updatedProduct = productRepository.findById(idProduct)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Category category = categoryRepository.findById(dto.getIdCategory())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        updatedProduct.setName(dto.getName());
        updatedProduct.setDescription(dto.getDescription());
        updatedProduct.setSalePrice(dto.getSalePrice());
        updatedProduct.setPurchasePrice(dto.getPurchasePrice());
        updatedProduct.setCategory(category);
        updatedProduct.setUpdatedAt(LocalDateTime.now());

        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                String oldImagePath = updatedProduct.getImage();
                if (oldImagePath != null && !oldImagePath.isEmpty()) {

                    String oldFilename = oldImagePath.replace("/uploads/", "");
                    Path oldImageFilePath = Paths.get("uploads", oldFilename);

                    File oldFile = oldImageFilePath.toFile();
                    if (oldFile.exists()) {
                        oldFile.delete();
                    }
                }
                String imageName = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();
                String uploadDir = "/app/uploads/";

                File directory = new File(uploadDir);
                if (!directory.exists()) {
                    directory.mkdirs();
                }

                Path imagePath = Paths.get(uploadDir, imageName);
                Files.copy(imageFile.getInputStream(), imagePath, StandardCopyOption.REPLACE_EXISTING);

                updatedProduct.setImage("/uploads/" + imageName);

            } catch (IOException e) {
                throw new RuntimeException("Failed to update image");
            }
        }

        return productRepository.save(updatedProduct);
    }
    public void activate(Integer idProduct){
        Product deactivatedProduct = productRepository.findById(idProduct)
                .orElseThrow(()-> new RuntimeException("Product not found"));
        deactivatedProduct.setStatus(true);
        deactivatedProduct.setUpdatedAt(LocalDateTime.now());
        productRepository.save(deactivatedProduct);
    }
    public void deactivate(Integer idProduct){
        Product deactivatedProduct = productRepository.findById(idProduct)
                .orElseThrow(()-> new RuntimeException("Product not found"));
        deactivatedProduct.setStatus(false);
        deactivatedProduct.setUpdatedAt(LocalDateTime.now());
        productRepository.save(deactivatedProduct);
    }
}
