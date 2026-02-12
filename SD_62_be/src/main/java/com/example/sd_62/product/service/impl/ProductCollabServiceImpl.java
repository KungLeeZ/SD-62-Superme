package com.example.sd_62.product.service.impl;

import com.example.sd_62.common.exception.ApiException;
import com.example.sd_62.common.util.MapperUtils;
import com.example.sd_62.product.dto.request.ProductCollabRequest;
import com.example.sd_62.product.dto.response.CollabResponse;
import com.example.sd_62.product.dto.response.ProductCollabResponse;
import com.example.sd_62.product.dto.response.ProductResponse;
import com.example.sd_62.product.entity.Collab;
import com.example.sd_62.product.entity.Product;
import com.example.sd_62.product.entity.ProductCollab;
import com.example.sd_62.product.repository.CollabRepository;
import com.example.sd_62.product.repository.ProductCollabRepository;
import com.example.sd_62.product.repository.ProductRepository;
import com.example.sd_62.product.service.ProductCollabService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductCollabServiceImpl implements ProductCollabService {

    private final ProductCollabRepository productCollabRepository;
    private final ProductRepository productRepository;
    private final CollabRepository collabRepository;

    // ==================== CRUD CƠ BẢN ====================

    @Override
    @Transactional
    public ProductCollabResponse save(Integer id, ProductCollabRequest dto) {
        // 1. Kiểm tra Product
        Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new ApiException("Không tìm thấy sản phẩm với ID: " + dto.getProductId(), "404"));

        // 2. Kiểm tra Collab
        Collab collab = collabRepository.findById(dto.getCollabId())
                .orElseThrow(() -> new ApiException("Không tìm thấy collab với ID: " + dto.getCollabId(), "404"));

        // 3. Kiểm tra trùng lặp
        if (id == null) {
            if (productCollabRepository.existsByProductAndCollab(product, collab)) {
                throw new ApiException("Sản phẩm đã được gán collab này rồi", "409");
            }
        } else {
            if (productCollabRepository.existsByProductIdAndCollabIdAndIdNot(
                    dto.getProductId(), dto.getCollabId(), id)) {
                throw new ApiException("Sản phẩm đã được gán collab này rồi", "409");
            }
        }

        // 4. Tạo hoặc cập nhật
        ProductCollab productCollab;
        if (id == null) {
            productCollab = ProductCollab.builder()
                    .product(product)
                    .collab(collab)
                    .build();
        } else {
            productCollab = productCollabRepository.findById(id)
                    .orElseThrow(() -> new ApiException("Không tìm thấy product collab với ID: " + id, "404"));
            productCollab.setProduct(product);
            productCollab.setCollab(collab);
        }

        productCollab = productCollabRepository.save(productCollab);
        log.info("✅ Saved product collab: Product {} - Collab {}", 
                product.getName(), collab.getName());

        return new ProductCollabResponse(productCollab);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        ProductCollab productCollab = productCollabRepository.findById(id)
                .orElseThrow(() -> new ApiException("Không tìm thấy product collab với ID: " + id, "404"));
        
        productCollabRepository.delete(productCollab);
        log.info("🗑️ Deleted product collab ID: {}", id);
    }

    @Override
    public ProductCollabResponse getById(Integer id) {
        return productCollabRepository.findById(id)
                .map(ProductCollabResponse::new)
                .orElseThrow(() -> new ApiException("Không tìm thấy product collab với ID: " + id, "404"));
    }

    @Override
    public List<ProductCollabResponse> getAll() {
        return productCollabRepository.findAll().stream()
                .map(ProductCollabResponse::new)
                .collect(Collectors.toList());
    }

    // ==================== QUẢN LÝ THEO PRODUCT ====================

    @Override
    public List<ProductCollabResponse> getByProductId(Integer productId) {
        if (!productRepository.existsById(productId)) {
            throw new ApiException("Không tìm thấy sản phẩm với ID: " + productId, "404");
        }
        
        return productCollabRepository.findByProductId(productId).stream()
                .map(ProductCollabResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<CollabResponse> getCollabsByProductId(Integer productId) {
        if (!productRepository.existsById(productId)) {
            throw new ApiException("Không tìm thấy sản phẩm với ID: " + productId, "404");
        }
        
        return productCollabRepository.findCollabsByProductId(productId).stream()
                .map(CollabResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<Integer> getCollabIdsByProductId(Integer productId) {
        return productCollabRepository.findCollabIdsByProductId(productId);
    }

    @Override
    @Transactional
    public void deleteByProductId(Integer productId) {
        if (!productRepository.existsById(productId)) {
            throw new ApiException("Không tìm thấy sản phẩm với ID: " + productId, "404");
        }
        
        productCollabRepository.deleteByProductId(productId);
        log.info("🗑️ Deleted all collabs for product ID: {}", productId);
    }

    @Override
    public long countByProductId(Integer productId) {
        return productCollabRepository.countByProductId(productId);
    }

    // ==================== QUẢN LÝ THEO COLLAB ====================

    @Override
    public List<ProductCollabResponse> getByCollabId(Integer collabId) {
        if (!collabRepository.existsById(collabId)) {
            throw new ApiException("Không tìm thấy collab với ID: " + collabId, "404");
        }
        
        return productCollabRepository.findByCollabId(collabId).stream()
                .map(ProductCollabResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductResponse> getProductsByCollabId(Integer collabId) {
        if (!collabRepository.existsById(collabId)) {
            throw new ApiException("Không tìm thấy collab với ID: " + collabId, "404");
        }
        
        return productCollabRepository.findProductsByCollabId(collabId).stream()
                .map(ProductResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<Integer> getProductIdsByCollabId(Integer collabId) {
        return productCollabRepository.findProductIdsByCollabId(collabId);
    }

    @Override
    @Transactional
    public void deleteByCollabId(Integer collabId) {
        if (!collabRepository.existsById(collabId)) {
            throw new ApiException("Không tìm thấy collab với ID: " + collabId, "404");
        }
        
        productCollabRepository.deleteByCollabId(collabId);
        log.info("🗑️ Deleted all products for collab ID: {}", collabId);
    }

    @Override
    public long countByCollabId(Integer collabId) {
        return productCollabRepository.countByCollabId(collabId);
    }

    // ==================== KIỂM TRA ====================

    @Override
    public boolean existsByProductIdAndCollabId(Integer productId, Integer collabId) {
        return productCollabRepository.existsByProductIdAndCollabId(productId, collabId);
    }

    @Override
    public boolean existsByProductIdAndCollabIdAndIdNot(Integer productId, Integer collabId, Integer id) {
        return productCollabRepository.existsByProductIdAndCollabIdAndIdNot(productId, collabId, id);
    }

    // ==================== BULK OPERATIONS ====================

    @Override
    @Transactional
    public List<ProductCollabResponse> saveBulk(Integer productId, List<Integer> collabIds) {
        // 1. Kiểm tra product
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ApiException("Không tìm thấy sản phẩm với ID: " + productId, "404"));
        
        List<ProductCollabResponse> responses = new ArrayList<>();
        List<String> errors = new ArrayList<>();
        
        // 2. Duyệt từng collabId
        for (Integer collabId : collabIds) {
            try {
                // Kiểm tra collab
                Collab collab = collabRepository.findById(collabId)
                        .orElseThrow(() -> new ApiException("Không tìm thấy collab với ID: " + collabId, "404"));
                
                // Kiểm tra trùng lặp
                if (!productCollabRepository.existsByProductAndCollab(product, collab)) {
                    ProductCollab productCollab = ProductCollab.builder()
                            .product(product)
                            .collab(collab)
                            .build();
                    
                    productCollab = productCollabRepository.save(productCollab);
                    responses.add(new ProductCollabResponse(productCollab));
                    log.info("✅ Added collab {} to product {}", collab.getName(), product.getName());
                }
            } catch (Exception e) {
                errors.add("Collab ID " + collabId + ": " + e.getMessage());
                log.error("❌ Failed to add collab {}: {}", collabId, e.getMessage());
            }
        }
        
        if (!errors.isEmpty()) {
            log.warn("⚠️ Some collabs were not added: {}", errors);
        }
        
        return responses;
    }

    @Override
    @Transactional
    public void deleteBulk(Integer productId, List<Integer> collabIds) {
        for (Integer collabId : collabIds) {
            productCollabRepository.deleteByProductIdAndCollabId(productId, collabId);
            log.info("🗑️ Removed collab {} from product {}", collabId, productId);
        }
    }

    @Override
    @Transactional
    public void syncCollabsForProduct(Integer productId, List<Integer> newCollabIds) {
        // 1. Lấy danh sách collab hiện tại
        List<Integer> currentCollabIds = productCollabRepository.findCollabIdsByProductId(productId);
        
        // 2. Tìm collab cần thêm (có trong new, không trong current)
        List<Integer> toAdd = newCollabIds.stream()
                .filter(id -> !currentCollabIds.contains(id))
                .collect(Collectors.toList());
        
        // 3. Tìm collab cần xóa (có trong current, không trong new)
        List<Integer> toRemove = currentCollabIds.stream()
                .filter(id -> !newCollabIds.contains(id))
                .collect(Collectors.toList());
        
        // 4. Thực hiện thêm
        if (!toAdd.isEmpty()) {
            saveBulk(productId, toAdd);
        }
        
        // 5. Thực hiện xóa
        if (!toRemove.isEmpty()) {
            deleteBulk(productId, toRemove);
        }
        
        log.info("🔄 Synced collabs for product {}: +{} -{}", 
                productId, toAdd.size(), toRemove.size());
    }
}