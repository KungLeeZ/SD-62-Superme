package com.example.sd_62.product.repository;

import com.example.sd_62.product.entity.ProductRelease;
import com.example.sd_62.product.entity.Season;
import com.example.sd_62.product.enums.DropStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductReleaseRepository extends JpaRepository<ProductRelease, Integer> {

    // ===== KIỂM TRA TỒN TẠI =====
    boolean existsByCodeIgnoreCase(String code);
    
    boolean existsByCodeIgnoreCaseAndIdNot(String code, Integer id);
    
    boolean existsByNameIgnoreCase(String name);
    
    boolean existsByNameIgnoreCaseAndIdNot(String name, Integer id);

    // ===== TÌM THEO TRẠNG THÁI =====
    List<ProductRelease> findByStatus(DropStatus status);
    
    Page<ProductRelease> findByStatus(DropStatus status, Pageable pageable);
    
    long countByStatus(DropStatus status);

    // ===== TÌM THEO MÙA =====
    List<ProductRelease> findBySeasonId(Integer seasonId);
    
    Page<ProductRelease> findBySeasonId(Integer seasonId, Pageable pageable);

    // ===== TÌM THEO THỜI GIAN =====
    List<ProductRelease> findByStartDateBetween(LocalDateTime from, LocalDateTime to);
    
    List<ProductRelease> findByEndDateBetween(LocalDateTime from, LocalDateTime to);
    
    @Query("SELECT pr FROM ProductRelease pr WHERE pr.startDate <= :now AND pr.endDate >= :now AND pr.status = :status")
    List<ProductRelease> findCurrentReleases(@Param("now") LocalDateTime now, @Param("status") DropStatus status);

    // ===== TÌM KIẾM CƠ BẢN =====
    Optional<ProductRelease> findByCode(String code);
    
    Optional<ProductRelease> findByName(String name);
    
    List<ProductRelease> findByCodeContainingIgnoreCaseOrNameContainingIgnoreCase(String code, String name);
    
    Page<ProductRelease> findByCodeContainingIgnoreCaseOrNameContainingIgnoreCase(
            String code, String name, Pageable pageable);

    // ===== SẮP XẾP =====
    List<ProductRelease> findAllByOrderByStartDateDesc();
    
    List<ProductRelease> findAllByOrderByCreatedAtDesc();
}