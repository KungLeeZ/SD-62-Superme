package com.example.sd_62.product.service;

import com.example.sd_62.product.dto.request.ProductReleaseRequest;
import com.example.sd_62.product.dto.response.ProductReleaseResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface ProductReleaseService {

    // ===== CRUD CƠ BẢN =====
    void save(Integer id, ProductReleaseRequest dto);
    
    void delete(Integer id);
    
    void restore(Integer id);
    
    ProductReleaseResponse getById(Integer id);
    
    List<ProductReleaseResponse> getAll(String status);
    
    Page<ProductReleaseResponse> getAllPaging(String status, Pageable pageable);

    // ===== TÌM THEO MÙA =====
    List<ProductReleaseResponse> getBySeasonId(Integer seasonId);
    
    Page<ProductReleaseResponse> getBySeasonIdPaging(Integer seasonId, Pageable pageable);

    // ===== TÌM THEO THỜI GIAN =====
    List<ProductReleaseResponse> getCurrentReleases();
    
    List<ProductReleaseResponse> getByDateRange(LocalDateTime from, LocalDateTime to);

    // ===== TÌM KIẾM =====
    ProductReleaseResponse getByCode(String code);
    
    ProductReleaseResponse getByName(String name);
    
    List<ProductReleaseResponse> search(String keyword);
    
    Page<ProductReleaseResponse> searchPaging(String keyword, Pageable pageable);

    // ===== KIỂM TRA =====
    boolean existsByCode(String code);
    
    boolean existsByCodeAndIdNot(String code, Integer id);
    
    boolean existsByName(String name);
    
    boolean existsByNameAndIdNot(String name, Integer id);

    // ===== THỐNG KÊ =====
    long countByStatus(String status);
}