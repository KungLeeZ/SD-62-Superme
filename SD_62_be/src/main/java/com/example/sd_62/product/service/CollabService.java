package com.example.sd_62.product.service;

import com.example.sd_62.product.dto.request.CollabRequest;
import com.example.sd_62.product.dto.response.CollabResponse;

import java.util.List;

public interface CollabService {

    // ===== CRUD CƠ BẢN =====
    void save(Integer id, CollabRequest dto);
    
    void delete(Integer id);
    
    CollabResponse getById(Integer id);
    
    List<CollabResponse> getAll();
    
    List<CollabResponse> getAllActive();

    // ===== TÌM KIẾM CƠ BẢN =====
    CollabResponse getByName(String name);
    
    List<CollabResponse> getByType(String type);
    
    List<CollabResponse> getByYear(Integer year);
    
    List<CollabResponse> getByYearRange(Integer startYear, Integer endYear);
    
    List<CollabResponse> getByTypeAndYear(String type, Integer year);

    // ===== KIỂM TRA =====
    boolean existsByName(String name);
    
    boolean existsByNameAndIdNot(String name, Integer id);
}