package com.example.sd_62.product.service;

import com.example.sd_62.product.dto.request.ProductCollabRequest;
import com.example.sd_62.product.dto.response.CollabResponse;
import com.example.sd_62.product.dto.response.ProductCollabResponse;
import com.example.sd_62.product.dto.response.ProductResponse;

import java.util.List;

public interface ProductCollabService {

    // ===== CRUD CƠ BẢN =====
    ProductCollabResponse save(Integer id, ProductCollabRequest dto);
    
    void delete(Integer id);
    
    ProductCollabResponse getById(Integer id);
    
    List<ProductCollabResponse> getAll();

    // ===== QUẢN LÝ THEO PRODUCT =====
    List<ProductCollabResponse> getByProductId(Integer productId);
    
    List<CollabResponse> getCollabsByProductId(Integer productId);
    
    List<Integer> getCollabIdsByProductId(Integer productId);
    
    void deleteByProductId(Integer productId);
    
    long countByProductId(Integer productId);

    // ===== QUẢN LÝ THEO COLLAB =====
    List<ProductCollabResponse> getByCollabId(Integer collabId);
    
    List<ProductResponse> getProductsByCollabId(Integer collabId);
    
    List<Integer> getProductIdsByCollabId(Integer collabId);
    
    void deleteByCollabId(Integer collabId);
    
    long countByCollabId(Integer collabId);

    // ===== KIỂM TRA =====
    boolean existsByProductIdAndCollabId(Integer productId, Integer collabId);
    
    boolean existsByProductIdAndCollabIdAndIdNot(Integer productId, Integer collabId, Integer id);

    // ===== BULK OPERATIONS =====
    List<ProductCollabResponse> saveBulk(Integer productId, List<Integer> collabIds);
    
    void deleteBulk(Integer productId, List<Integer> collabIds);
    
    void syncCollabsForProduct(Integer productId, List<Integer> collabIds);
}