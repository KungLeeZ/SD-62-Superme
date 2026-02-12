package com.example.sd_62.product.controller;

import com.example.sd_62.product.dto.request.ProductRequest;
import com.example.sd_62.product.dto.request.ProductSearchRequest;
import com.example.sd_62.product.dto.response.ApiResponse;
import com.example.sd_62.product.dto.response.ProductResponse;
import com.example.sd_62.product.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Tag(name = "Product", description = "API quản lý sản phẩm")
public class ProductController {

    private final ProductService productService;

    // ==================== CRUD CƠ BẢN ====================

    @PostMapping
    @Operation(summary = "Tạo sản phẩm mới")
    public ResponseEntity<ApiResponse<Void>> createProduct(@Valid @RequestBody ProductRequest request) {
        productService.save(null, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Tạo sản phẩm thành công", null));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Cập nhật sản phẩm")
    public ResponseEntity<ApiResponse<Void>> updateProduct(
            @Parameter(description = "ID sản phẩm") @PathVariable Integer id,
            @Valid @RequestBody ProductRequest request) {
        productService.save(id, request);
        return ResponseEntity.ok(ApiResponse.success("Cập nhật sản phẩm thành công", null));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Xóa mềm sản phẩm")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(
            @Parameter(description = "ID sản phẩm") @PathVariable Integer id) {
        productService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Xóa sản phẩm thành công", null));
    }

    @PatchMapping("/{id}/restore")
    @Operation(summary = "Khôi phục sản phẩm đã xóa")
    public ResponseEntity<ApiResponse<Void>> restoreProduct(
            @Parameter(description = "ID sản phẩm") @PathVariable Integer id) {
        productService.restore(id);
        return ResponseEntity.ok(ApiResponse.success("Khôi phục sản phẩm thành công", null));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Lấy chi tiết sản phẩm theo ID")
    public ResponseEntity<ApiResponse<ProductResponse>> getProductById(
            @Parameter(description = "ID sản phẩm") @PathVariable Integer id) {
        ProductResponse response = productService.getById(id);
        return ResponseEntity.ok(ApiResponse.success("Lấy thông tin sản phẩm thành công", response));
    }

    // ==================== LẤY DANH SÁCH ====================

    @GetMapping
    @Operation(summary = "Lấy tất cả sản phẩm (không phân trang)")
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getAllProducts(
            @Parameter(description = "Lọc theo status (ACTIVE/INACTIVE)")
            @RequestParam(required = false) String status) {
        List<ProductResponse> responses = productService.getAll(status);
        return ResponseEntity.ok(ApiResponse.success("Lấy danh sách sản phẩm thành công", responses));
    }

    @GetMapping("/paging")
    @Operation(summary = "Lấy danh sách sản phẩm có phân trang")
    public ResponseEntity<ApiResponse<Page<ProductResponse>>> getAllProductsPaging(
            @Parameter(description = "Lọc theo status (ACTIVE/INACTIVE)")
            @RequestParam(required = false) String status,
            
            @Parameter(description = "Số trang (bắt đầu từ 0)")
            @RequestParam(defaultValue = "0") int page,
            
            @Parameter(description = "Kích thước trang")
            @RequestParam(defaultValue = "10") int size,
            
            @Parameter(description = "Sắp xếp theo trường (ví dụ: name, createdAt)")
            @RequestParam(defaultValue = "createdAt") String sortBy,
            
            @Parameter(description = "Hướng sắp xếp (ASC/DESC)")
            @RequestParam(defaultValue = "DESC") String sortDirection) {
        
        Sort.Direction direction = Sort.Direction.fromString(sortDirection.toUpperCase());
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        
        Page<ProductResponse> responses = productService.getAllPaging(status, pageable);
        return ResponseEntity.ok(ApiResponse.success("Lấy danh sách sản phẩm thành công", responses));
    }

    // ==================== TÌM KIẾM NÂNG CAO ====================

    @PostMapping("/search")
    @Operation(summary = "Tìm kiếm sản phẩm nâng cao (có phân trang)")
    public ResponseEntity<ApiResponse<Page<ProductResponse>>> searchProducts(
            @Valid @RequestBody ProductSearchRequest searchRequest,
            
            @Parameter(description = "Số trang (bắt đầu từ 0)")
            @RequestParam(defaultValue = "0") int page,
            
            @Parameter(description = "Kích thước trang")
            @RequestParam(defaultValue = "10") int size,
            
            @Parameter(description = "Sắp xếp theo trường (ví dụ: name, createdAt)")
            @RequestParam(required = false) String sortBy,
            
            @Parameter(description = "Hướng sắp xếp (ASC/DESC)")
            @RequestParam(defaultValue = "ASC") String sortDirection) {
        
        Pageable pageable;
        if (sortBy != null && !sortBy.isEmpty()) {
            Sort.Direction direction = Sort.Direction.fromString(sortDirection.toUpperCase());
            pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        } else {
            pageable = PageRequest.of(page, size);
        }
        
        Page<ProductResponse> responses = productService.searchProducts(searchRequest, pageable);
        return ResponseEntity.ok(ApiResponse.success("Tìm kiếm sản phẩm thành công", responses));
    }

    @PostMapping("/search/all")
    @Operation(summary = "Tìm kiếm sản phẩm nâng cao (không phân trang)")
    public ResponseEntity<ApiResponse<List<ProductResponse>>> searchAllProducts(
            @Valid @RequestBody ProductSearchRequest searchRequest) {
        List<ProductResponse> responses = productService.searchProducts(searchRequest);
        return ResponseEntity.ok(ApiResponse.success("Tìm kiếm sản phẩm thành công", responses));
    }

    // ==================== CÁC API BỔ SUNG ====================

    @GetMapping("/count")
    @Operation(summary = "Đếm số lượng sản phẩm theo status")
    public ResponseEntity<ApiResponse<Long>> countByStatus(
            @Parameter(description = "Status (ACTIVE/INACTIVE)")
            @RequestParam String status) {
        long count = productService.countByStatus(status);
        return ResponseEntity.ok(ApiResponse.success("Đếm sản phẩm thành công", count));
    }

    @GetMapping("/check-code")
    @Operation(summary = "Kiểm tra mã sản phẩm đã tồn tại chưa")
    public ResponseEntity<ApiResponse<Boolean>> checkProductCode(
            @Parameter(description = "Mã sản phẩm")
            @RequestParam String productCode,
            
            @Parameter(description = "ID sản phẩm (khi cập nhật)")
            @RequestParam(required = false) Integer id) {
        
        boolean exists;
        if (id == null) {
            exists = productService.existsByProductCode(productCode);
        } else {
            exists = productService.existsByProductCodeAndIdNot(productCode, id);
        }
        
        return ResponseEntity.ok(ApiResponse.success("Kiểm tra mã sản phẩm thành công", exists));
    }
}