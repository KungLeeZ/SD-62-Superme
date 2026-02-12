package com.example.sd_62.product.service;

import com.example.sd_62.product.dto.request.MaterialRequest;
import com.example.sd_62.product.dto.response.MaterialResponse;

import java.util.List;

public interface MaterialService {

    // ===== CRUD CƠ BẢN =====
    void save(Integer id, MaterialRequest dto);
    
    void delete(Integer id);
    
    MaterialResponse getById(Integer id);
    
    List<MaterialResponse> getAll();
    
    // ===== TÌM KIẾM CƠ BẢN =====
    MaterialResponse getByName(String name);
    
    List<MaterialResponse> searchByName(String keyword);
    
    // ===== KIỂM TRA =====
    boolean existsByName(String name);
    
    boolean existsByNameAndIdNot(String name, Integer id);
}