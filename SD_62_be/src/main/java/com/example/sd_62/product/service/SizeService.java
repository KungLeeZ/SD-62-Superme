package com.example.sd_62.product.service;

import com.example.sd_62.product.dto.response.SizeResponse;

import java.math.BigDecimal;
import java.util.List;

public interface SizeService {

    // Lấy tất cả sizes
    List<SizeResponse> getAll();
    
    // Lấy size theo ID
    SizeResponse getById(Integer id);
    
    // Tìm size theo các chuẩn
    SizeResponse getByJp(BigDecimal jp);
    
    SizeResponse getByEu(BigDecimal eu);
    
    SizeResponse getByUsMen(BigDecimal usMen);
    
    SizeResponse getByUsWomen(BigDecimal usWomen);
    
    SizeResponse getByFootLength(BigDecimal footLength);
    
    // Tìm size trong khoảng
    List<SizeResponse> getByJpRange(BigDecimal from, BigDecimal to);
}