package com.example.sd_62.product.service;

import com.example.sd_62.product.dto.request.ProductVariantRequest;
import com.example.sd_62.product.dto.response.ProductVariantResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface ProductVariantService {

    // ===== CRUD CƠ BẢN =====
    void save(Integer id, ProductVariantRequest dto);
    
    void delete(Integer id);
    
    void restore(Integer id);
    
    ProductVariantResponse getById(Integer id);
    
    List<ProductVariantResponse> getAll(String status);

    // ===== PHÂN TRANG =====
    Page<ProductVariantResponse> getAllPaging(String status, Pageable pageable);

    // ===== TÌM KIẾM THEO SẢN PHẨM =====
    List<ProductVariantResponse> getByProductId(Integer productId);
    
    Page<ProductVariantResponse> getByProductIdPaging(Integer productId, Pageable pageable);

    // ===== TÌM KIẾM NÂNG CAO =====
    Page<ProductVariantResponse> searchVariants(
            Integer productId,
            Integer sizeId,
            Integer colorId,
            String keyword,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            String status,
            Pageable pageable);

    // ===== KIỂM TRA TỒN TẠI =====
    boolean existsBySkuVariant(String skuVariant);
    
    boolean existsBySkuVariantAndIdNot(String skuVariant, Integer id);
    
    boolean existsByProductAndSizeAndColor(Integer productId, Integer sizeId, Integer colorId, Integer id);

    // ===== THỐNG KÊ =====
    long countByProductId(Integer productId);
    
    long countByStatus(String status);
    
    Integer getTotalQuantityByProductId(Integer productId);
    
    List<Map<String, Object>> getTopProductsByQuantity(int limit);

    // ===== QUẢN LÝ TỒN KHO =====
    void updateQuantity(Integer id, Integer quantity);
    
    void decreaseQuantity(Integer id, Integer quantity);
    
    void increaseQuantity(Integer id, Integer quantity);
    
    boolean checkAvailableQuantity(Integer id, Integer requestedQuantity);
}