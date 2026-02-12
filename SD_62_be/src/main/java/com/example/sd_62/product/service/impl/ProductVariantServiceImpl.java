package com.example.sd_62.product.service.impl;

import com.example.sd_62.common.exception.ApiException;
import com.example.sd_62.common.util.MapperUtils;
import com.example.sd_62.product.dto.request.ProductVariantRequest;
import com.example.sd_62.product.dto.response.ProductVariantResponse;
import com.example.sd_62.product.entity.*;
import com.example.sd_62.product.enums.ProductStatus;
import com.example.sd_62.product.repository.*;
import com.example.sd_62.product.service.ProductVariantService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductVariantServiceImpl implements ProductVariantService {

    private final ProductVariantRepository productVariantRepository;
    private final ProductRepository productRepository;
    private final SizeRepository sizeRepository;
    private final ColorRepository colorRepository;

    // ==================== CRUD CƠ BẢN ====================

    @Transactional
    @Override
    public void save(Integer id, ProductVariantRequest dto) {
        // 1. Kiểm tra SKU variant
        if (id == null) {
            if (productVariantRepository.existsBySkuVariantIgnoreCase(dto.getSkuVariant())) {
                throw new ApiException("Mã SKU variant đã tồn tại", "409");
            }
        } else {
            if (productVariantRepository.existsBySkuVariantIgnoreCaseAndIdNot(dto.getSkuVariant(), id)) {
                throw new ApiException("Mã SKU variant đã tồn tại", "409");
            }
        }

        // 2. Kiểm tra Product
        Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new ApiException("Không tìm thấy sản phẩm có ID: " + dto.getProductId(), "404"));

        // 3. Kiểm tra Size
        Size size = sizeRepository.findById(dto.getSizeId())
                .orElseThrow(() -> new ApiException("Không tìm thấy size có ID: " + dto.getSizeId(), "404"));

        // 4. Kiểm tra Color
        Color color = colorRepository.findById(dto.getColorId())
                .orElseThrow(() -> new ApiException("Không tìm thấy màu sắc có ID: " + dto.getColorId(), "404"));

        // 5. Kiểm tra trùng lặp Product + Size + Color
        if (id == null) {
            if (productVariantRepository.existsByProductAndSizeAndColor(product, size, color)) {
                throw new ApiException("Variant với sản phẩm, size và màu này đã tồn tại", "409");
            }
        } else {
            if (productVariantRepository.existsByProductAndSizeAndColorAndIdNot(product, size, color, id)) {
                throw new ApiException("Variant với sản phẩm, size và màu này đã tồn tại", "409");
            }
        }

        // 6. Tạo hoặc cập nhật variant
        ProductVariant variant;
        if (id == null) {
            variant = MapperUtils.map(dto, ProductVariant.class);
            variant.setCreatedAt(LocalDateTime.now());
            variant.setStatus(ProductStatus.ACTIVE);
        } else {
            variant = productVariantRepository.findById(id)
                    .orElseThrow(() -> new ApiException("Không tìm thấy variant có ID: " + id, "404"));
            MapperUtils.mapToExisting(dto, variant);
            variant.setUpdatedAt(LocalDateTime.now());
        }

        // 7. Set các quan hệ
        variant.setProduct(product);
        variant.setSize(size);
        variant.setColor(color);

        productVariantRepository.save(variant);
        log.info("✅ Saved product variant: {}", variant.getSkuVariant());
    }

    @Transactional
    @Override
    public void delete(Integer id) {
        ProductVariant variant = productVariantRepository.findById(id)
                .orElseThrow(() -> new ApiException("Không tìm thấy variant có ID: " + id, "404"));

        if (variant.getStatus() == ProductStatus.INACTIVE) {
            throw new ApiException("Variant đã ở trạng thái INACTIVE", "400");
        }

        variant.setStatus(ProductStatus.INACTIVE);
        variant.setUpdatedAt(LocalDateTime.now());
        productVariantRepository.save(variant);
        
        log.info("🗑️ Soft deleted product variant ID: {}", id);
    }

    @Transactional
    @Override
    public void restore(Integer id) {
        ProductVariant variant = productVariantRepository.findById(id)
                .orElseThrow(() -> new ApiException("Không tìm thấy variant có ID: " + id, "404"));

        if (variant.getStatus() == ProductStatus.ACTIVE) {
            throw new ApiException("Variant đang ở trạng thái ACTIVE", "400");
        }

        variant.setStatus(ProductStatus.ACTIVE);
        variant.setUpdatedAt(LocalDateTime.now());
        productVariantRepository.save(variant);
        
        log.info("🔄 Restored product variant ID: {}", id);
    }

    @Override
    public ProductVariantResponse getById(Integer id) {
        return productVariantRepository.findById(id)
                .map(ProductVariantResponse::new)
                .orElseThrow(() -> new ApiException("Không tìm thấy variant có ID: " + id, "404"));
    }

    @Override
    public List<ProductVariantResponse> getAll(String status) {
        List<ProductVariant> variants;
        
        if (status != null && !status.isEmpty()) {
            try {
                ProductStatus productStatus = ProductStatus.valueOf(status.toUpperCase());
                variants = productVariantRepository.findAll().stream()
                        .filter(v -> v.getStatus() == productStatus)
                        .collect(Collectors.toList());
            } catch (IllegalArgumentException e) {
                throw new ApiException("Status không hợp lệ: " + status, "400");
            }
        } else {
            variants = productVariantRepository.findAll();
        }

        return variants.stream()
                .map(ProductVariantResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public Page<ProductVariantResponse> getAllPaging(String status, Pageable pageable) {
        Page<ProductVariant> variantPage;

        if (status != null && !status.isEmpty()) {
            try {
                ProductStatus productStatus = ProductStatus.valueOf(status.toUpperCase());
                // ✅ ĐÃ SỬA - Giờ đây method này đã tồn tại trong Repository
                variantPage = productVariantRepository.findByStatus(productStatus, pageable);
            } catch (IllegalArgumentException e) {
                throw new ApiException("Status không hợp lệ: " + status +
                        ". Chấp nhận: ACTIVE, INACTIVE, OUT_OF_STOCK, DISCONTINUED", "400");
            }
        } else {
            variantPage = productVariantRepository.findAll(pageable);
        }

        return variantPage.map(ProductVariantResponse::new);
    }

    // ==================== TÌM KIẾM THEO SẢN PHẨM ====================

    @Override
    public List<ProductVariantResponse> getByProductId(Integer productId) {
        if (!productRepository.existsById(productId)) {
            throw new ApiException("Không tìm thấy sản phẩm có ID: " + productId, "404");
        }
        
        return productVariantRepository.findByProductId(productId).stream()
                .map(ProductVariantResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public Page<ProductVariantResponse> getByProductIdPaging(Integer productId, Pageable pageable) {
        if (!productRepository.existsById(productId)) {
            throw new ApiException("Không tìm thấy sản phẩm có ID: " + productId, "404");
        }
        
        return productVariantRepository.findByProductId(productId, pageable)
                .map(ProductVariantResponse::new);
    }

    // ==================== TÌM KIẾM NÂNG CAO ====================

    @Override
    public Page<ProductVariantResponse> searchVariants(
            Integer productId,
            Integer sizeId,
            Integer colorId,
            String keyword,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            String status,
            Pageable pageable) {
        
        ProductStatus productStatus = null;
        if (status != null && !status.isEmpty()) {
            try {
                productStatus = ProductStatus.valueOf(status.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new ApiException("Status không hợp lệ: " + status, "400");
            }
        }

        Page<ProductVariant> variantPage = productVariantRepository.searchVariants(
                productId, sizeId, colorId, keyword, minPrice, maxPrice, productStatus, pageable);

        return variantPage.map(ProductVariantResponse::new);
    }

    // ==================== KIỂM TRA TỒN TẠI ====================

    @Override
    public boolean existsBySkuVariant(String skuVariant) {
        return productVariantRepository.existsBySkuVariantIgnoreCase(skuVariant);
    }

    @Override
    public boolean existsBySkuVariantAndIdNot(String skuVariant, Integer id) {
        return productVariantRepository.existsBySkuVariantIgnoreCaseAndIdNot(skuVariant, id);
    }

    @Override
    public boolean existsByProductAndSizeAndColor(Integer productId, Integer sizeId, Integer colorId, Integer id) {
        Product product = productRepository.getReferenceById(productId);
        Size size = sizeRepository.getReferenceById(sizeId);
        Color color = colorRepository.getReferenceById(colorId);
        
        if (id == null) {
            return productVariantRepository.existsByProductAndSizeAndColor(product, size, color);
        } else {
            return productVariantRepository.existsByProductAndSizeAndColorAndIdNot(product, size, color, id);
        }
    }

    // ==================== THỐNG KÊ ====================

    @Override
    public long countByProductId(Integer productId) {
        return productVariantRepository.countByProductId(productId);
    }

    @Override
    public long countByStatus(String status) {
        try {
            ProductStatus productStatus = ProductStatus.valueOf(status.toUpperCase());
            return productVariantRepository.countByStatus(productStatus);
        } catch (IllegalArgumentException e) {
            throw new ApiException("Status không hợp lệ: " + status, "400");
        }
    }

    @Override
    public Integer getTotalQuantityByProductId(Integer productId) {
        return productVariantRepository.getTotalQuantityByProductId(productId);
    }

    @Override
    public List<Map<String, Object>> getTopProductsByQuantity(int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        List<Object[]> results = productVariantRepository.getTopProductsByQuantity(pageable);
        
        List<Map<String, Object>> topProducts = new ArrayList<>();
        for (Object[] result : results) {
            Map<String, Object> map = new HashMap<>();
            map.put("productId", result[0]);
            
            Integer productId = (Integer) result[0];
            Product product = productRepository.findById(productId).orElse(null);
            map.put("productName", product != null ? product.getName() : "Unknown");
            map.put("totalQuantity", result[1]);
            
            topProducts.add(map);
        }
        
        return topProducts;
    }

    // ==================== QUẢN LÝ TỒN KHO ====================

    @Transactional
    @Override
    public void updateQuantity(Integer id, Integer quantity) {
        if (quantity < 0) {
            throw new ApiException("Số lượng không thể âm", "400");
        }
        
        ProductVariant variant = productVariantRepository.findById(id)
                .orElseThrow(() -> new ApiException("Không tìm thấy variant có ID: " + id, "404"));
        
        variant.setQuantity(quantity);
        variant.setUpdatedAt(LocalDateTime.now());
        productVariantRepository.save(variant);
        
        log.info("📦 Updated quantity for variant ID {}: {}", id, quantity);
    }

    @Transactional
    @Override
    public void decreaseQuantity(Integer id, Integer quantity) {
        if (quantity <= 0) {
            throw new ApiException("Số lượng giảm phải lớn hơn 0", "400");
        }
        
        ProductVariant variant = productVariantRepository.findById(id)
                .orElseThrow(() -> new ApiException("Không tìm thấy variant có ID: " + id, "404"));
        
        if (variant.getQuantity() < quantity) {
            throw new ApiException("Số lượng tồn kho không đủ", "400");
        }
        
        variant.setQuantity(variant.getQuantity() - quantity);
        variant.setUpdatedAt(LocalDateTime.now());
        productVariantRepository.save(variant);
        
        log.info("📉 Decreased quantity for variant ID {} by {}: new quantity {}", 
            id, quantity, variant.getQuantity());
    }

    @Transactional
    @Override
    public void increaseQuantity(Integer id, Integer quantity) {
        if (quantity <= 0) {
            throw new ApiException("Số lượng tăng phải lớn hơn 0", "400");
        }
        
        ProductVariant variant = productVariantRepository.findById(id)
                .orElseThrow(() -> new ApiException("Không tìm thấy variant có ID: " + id, "404"));
        
        variant.setQuantity(variant.getQuantity() + quantity);
        variant.setUpdatedAt(LocalDateTime.now());
        productVariantRepository.save(variant);
        
        log.info("📈 Increased quantity for variant ID {} by {}: new quantity {}", 
            id, quantity, variant.getQuantity());
    }

    @Override
    public boolean checkAvailableQuantity(Integer id, Integer requestedQuantity) {
        ProductVariant variant = productVariantRepository.findById(id)
                .orElseThrow(() -> new ApiException("Không tìm thấy variant có ID: " + id, "404"));
        
        return variant.getQuantity() >= requestedQuantity;
    }
}