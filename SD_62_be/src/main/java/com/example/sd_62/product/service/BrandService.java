package com.example.sd_62.product.service;

import com.example.sd_62.product.dto.request.BrandRequest;
import com.example.sd_62.product.dto.response.BrandResponse;
import com.example.sd_62.product.enums.BrandStatus;

import java.util.List;

public interface BrandService {

    // ===== CRUD CƠ BẢN =====
    void save(Integer id, BrandRequest dto);
    
    void delete(Integer id);
    
    void restore(Integer id);
    
    BrandResponse getById(Integer id);
    
    List<BrandResponse> getAll(String status);
    
    // ===== TÌM KIẾM CƠ BẢN =====
    BrandResponse getByCode(String code);
    
    BrandResponse getByName(String name);
    
    List<BrandResponse> getByStatus(BrandStatus status);
    
    // ===== KIỂM TRA =====
    boolean existsByCode(String code);
    
    boolean existsByCodeAndIdNot(String code, Integer id);
    
    boolean existsByName(String name);
    
    boolean existsByNameAndIdNot(String name, Integer id);
    
    // ===== THỐNG KÊ =====
    long countByStatus(String status);
}