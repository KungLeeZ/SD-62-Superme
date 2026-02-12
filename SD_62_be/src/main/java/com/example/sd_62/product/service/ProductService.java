package com.example.sd_62.product.service;

import com.example.sd_62.product.dto.request.ProductRequest;
import com.example.sd_62.product.dto.request.ProductSearchRequest;
import com.example.sd_62.product.dto.response.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {
    // CRUD cơ bản
    void save(Integer id, ProductRequest dto);
    void delete(Integer id);
    void restore(Integer id);
    ProductResponse getById(Integer id);
    List<ProductResponse> getAll(String status);

    // Phân trang
    Page<ProductResponse> getAllPaging(String status, Pageable pageable);

    // Tìm kiếm nâng cao
    Page<ProductResponse> searchProducts(ProductSearchRequest searchRequest, Pageable pageable);
    List<ProductResponse> searchProducts(ProductSearchRequest searchRequest);

    // Kiểm tra tồn tại
    boolean existsByProductCode(String productCode);
    boolean existsByProductCodeAndIdNot(String productCode, Integer id);

    // Thống kê
    long countByStatus(String status);
}