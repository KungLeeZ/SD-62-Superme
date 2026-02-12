package com.example.sd_62.product.service;

import com.example.sd_62.product.dto.request.ColorRequest;
import com.example.sd_62.product.dto.response.ColorResponse;

import java.util.List;

public interface ColorService {

    // ===== CRUD CƠ BẢN =====
    void save(Integer id, ColorRequest dto);
    
    void delete(Integer id);
    
    ColorResponse getById(Integer id);
    
    List<ColorResponse> getAll();
    
    // ===== TÌM KIẾM CƠ BẢN =====
    ColorResponse getByName(String name);
    
    ColorResponse getByHex(String hex);
    
    List<ColorResponse> searchByName(String keyword);
    
    // ===== KIỂM TRA =====
    boolean existsByName(String name);
    
    boolean existsByNameAndIdNot(String name, Integer id);
    
    boolean existsByHex(String hex);
    
    boolean existsByHexAndIdNot(String hex, Integer id);
}