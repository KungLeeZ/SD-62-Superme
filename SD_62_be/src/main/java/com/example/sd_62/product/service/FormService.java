package com.example.sd_62.product.service;

import com.example.sd_62.product.dto.request.FormRequest;
import com.example.sd_62.product.dto.response.FormResponse;

import java.util.List;

public interface FormService {

    // ===== CRUD CƠ BẢN =====
    void save(Integer id, FormRequest dto);
    
    void delete(Integer id);
    
    FormResponse getById(Integer id);
    
    List<FormResponse> getAll();
    
    // ===== TÌM KIẾM CƠ BẢN =====
    FormResponse getByName(String name);
    
    List<FormResponse> searchByName(String keyword);
    
    // ===== KIỂM TRA =====
    boolean existsByName(String name);
    
    boolean existsByNameAndIdNot(String name, Integer id);
}