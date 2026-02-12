package com.example.sd_62.product.repository;

import com.example.sd_62.product.entity.ProductImage;
import com.example.sd_62.product.entity.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ProductImageRepository extends JpaRepository<ProductImage, Integer> {

    // Lấy tất cả ảnh của variant, sắp xếp theo thứ tự
    List<ProductImage> findByProductVariantOrderBySortOrderAsc(ProductVariant productVariant);
    
    // Lấy tất cả ảnh của variant theo variantId
    @Query("SELECT pi FROM ProductImage pi WHERE pi.productVariant.id = :variantId ORDER BY pi.sortOrder ASC")
    List<ProductImage> findByVariantId(@Param("variantId") Integer variantId);
    
    // Xóa tất cả ảnh của variant
    @Modifying
    @Transactional
    @Query("DELETE FROM ProductImage pi WHERE pi.productVariant.id = :variantId")
    void deleteByVariantId(@Param("variantId") Integer variantId);
    
    // Đếm số ảnh của variant
    long countByProductVariantId(Integer variantId);
    
    // Kiểm tra ảnh có thuộc variant không
    boolean existsByIdAndProductVariantId(Integer imageId, Integer variantId);
    
    // Lấy sortOrder lớn nhất của variant
    @Query("SELECT MAX(pi.sortOrder) FROM ProductImage pi WHERE pi.productVariant.id = :variantId")
    Integer findMaxSortOrderByVariantId(@Param("variantId") Integer variantId);
    
    // Lấy tất cả imageUrl để cleanup
    @Query("SELECT pi.imageUrl FROM ProductImage pi")
    List<String> findAllImageUrls();
}