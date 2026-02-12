package com.example.sd_62.product.repository;

import com.example.sd_62.product.entity.Product;
import com.example.sd_62.product.enums.ProductStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    // Kiểm tra trùng productCode
    boolean existsByProductCodeIgnoreCase(String productCode);
    boolean existsByProductCodeIgnoreCaseAndIdNot(String productCode, Integer id);

    // Phân trang theo status
    Page<Product> findByStatus(ProductStatus status, Pageable pageable);
    Page<Product> findAll(Pageable pageable);

    // Tìm kiếm nâng cao với Query
    @Query("SELECT p FROM Product p WHERE " +
            "(:keyword IS NULL OR LOWER(p.productCode) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
            "AND (:brandId IS NULL OR p.brand.id = :brandId) " +
            "AND (:genderId IS NULL OR p.gender.id = :genderId) " +
            "AND (:materialId IS NULL OR p.material.id = :materialId) " +
            "AND (:formId IS NULL OR p.form.id = :formId) " +
            "AND (:productReleaseId IS NULL OR p.productRelease.id = :productReleaseId) " +
            "AND (:status IS NULL OR p.status = :status) " +
            "AND (:fromDate IS NULL OR p.createdAt >= :fromDate) " +
            "AND (:toDate IS NULL OR p.createdAt <= :toDate)")
    Page<Product> searchProducts(@Param("keyword") String keyword,
                                 @Param("brandId") Integer brandId,
                                 @Param("genderId") Integer genderId,
                                 @Param("materialId") Integer materialId,
                                 @Param("formId") Integer formId,
                                 @Param("productReleaseId") Integer productReleaseId,
                                 @Param("status") ProductStatus status,
                                 @Param("fromDate") LocalDateTime fromDate,
                                 @Param("toDate") LocalDateTime toDate,
                                 Pageable pageable);

    // Đếm theo status
    long countByStatus(ProductStatus status);
}