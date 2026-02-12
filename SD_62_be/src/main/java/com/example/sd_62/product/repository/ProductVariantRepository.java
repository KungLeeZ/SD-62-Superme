package com.example.sd_62.product.repository;

import com.example.sd_62.product.entity.Color;
import com.example.sd_62.product.entity.Product;
import com.example.sd_62.product.entity.ProductVariant;
import com.example.sd_62.product.entity.Size;
import com.example.sd_62.product.enums.ProductStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductVariantRepository extends JpaRepository<ProductVariant, Integer> {

    // ===== KIỂM TRA TỒN TẠI =====
    boolean existsBySkuVariantIgnoreCase(String skuVariant);
    
    boolean existsBySkuVariantIgnoreCaseAndIdNot(String skuVariant, Integer id);
    
    boolean existsByProductAndSizeAndColorAndIdNot(
        Product product, Size size, Color color, Integer id);
    
    boolean existsByProductAndSizeAndColor(
        Product product, Size size, Color color);

    // ===== TÌM THEO ĐIỀU KIỆN =====
    List<ProductVariant> findByProductId(Integer productId);
    
    Page<ProductVariant> findByProductId(Integer productId, Pageable pageable);
    
    List<ProductVariant> findByProductIdAndStatus(Integer productId, ProductStatus status);
    
    List<ProductVariant> findBySizeId(Integer sizeId);
    
    List<ProductVariant> findByColorId(Integer colorId);
    
    Optional<ProductVariant> findByProductIdAndSizeIdAndColorId(
        Integer productId, Integer sizeId, Integer colorId);

    // ===== TÌM KIẾM NÂNG CAO =====
    @Query("SELECT pv FROM ProductVariant pv WHERE " +
           "(:productId IS NULL OR pv.product.id = :productId) " +
           "AND (:sizeId IS NULL OR pv.size.id = :sizeId) " +
           "AND (:colorId IS NULL OR pv.color.id = :colorId) " +
           "AND (:keyword IS NULL OR LOWER(pv.skuVariant) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "OR LOWER(pv.description) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
           "AND (:minPrice IS NULL OR pv.price >= :minPrice) " +
           "AND (:maxPrice IS NULL OR pv.price <= :maxPrice) " +
           "AND (:status IS NULL OR pv.status = :status)")
    Page<ProductVariant> searchVariants(
            @Param("productId") Integer productId,
            @Param("sizeId") Integer sizeId,
            @Param("colorId") Integer colorId,
            @Param("keyword") String keyword,
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice,
            @Param("status") ProductStatus status,
            Pageable pageable);

    // ===== THỐNG KÊ =====
    long countByProductId(Integer productId);
    
    long countByStatus(ProductStatus status);
    
    @Query("SELECT SUM(pv.quantity) FROM ProductVariant pv WHERE pv.product.id = :productId")
    Integer getTotalQuantityByProductId(@Param("productId") Integer productId);
    
    @Query("SELECT pv.product.id, SUM(pv.quantity) FROM ProductVariant pv " +
           "GROUP BY pv.product.id ORDER BY SUM(pv.quantity) DESC")
    List<Object[]> getTopProductsByQuantity(Pageable pageable);

    Page<ProductVariant> findByStatus(ProductStatus status, Pageable pageable);
}