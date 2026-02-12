package com.example.sd_62.product.service;

import com.example.sd_62.product.dto.request.SeasonRequest;
import com.example.sd_62.product.dto.response.SeasonResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SeasonService {

    // ===== CRUD CƠ BẢN =====
    void save(Integer id, SeasonRequest dto);
    
    void delete(Integer id);
    
    SeasonResponse getById(Integer id);
    
    List<SeasonResponse> getAll();
    
    Page<SeasonResponse> getAllPaging(Pageable pageable);

    // ===== TÌM KIẾM =====
    SeasonResponse getBySeasonCode(String seasonCode);
    
    SeasonResponse getByName(String name);
    
    List<SeasonResponse> getByYear(Integer year);
    
    List<SeasonResponse> getByYearRange(Integer startYear, Integer endYear);
    
    List<SeasonResponse> search(String keyword);
    
    Page<SeasonResponse> searchPaging(String keyword, Pageable pageable);

    // ===== KIỂM TRA =====
    boolean existsBySeasonCode(String seasonCode);
    
    boolean existsBySeasonCodeAndIdNot(String seasonCode, Integer id);
    
    boolean existsByName(String name);
    
    boolean existsByNameAndIdNot(String name, Integer id);

    // ===== THỐNG KÊ =====
    long countByYear(Integer year);
}