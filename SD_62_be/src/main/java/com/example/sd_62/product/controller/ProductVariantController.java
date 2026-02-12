package com.example.sd_62.product.controller;

import com.example.sd_62.product.dto.request.ProductVariantRequest;
import com.example.sd_62.product.dto.request.ProductVariantSearchRequest;
import com.example.sd_62.product.dto.response.ApiResponse;
import com.example.sd_62.product.dto.response.ProductVariantResponse;
import com.example.sd_62.product.service.ProductVariantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/product-variants")
@RequiredArgsConstructor
@Tag(name = "Product Variant", description = "API quản lý biến thể sản phẩm")
public class ProductVariantController {

    private final ProductVariantService productVariantService;

    // ==================== CRUD CƠ BẢN ====================

    @PostMapping
    @Operation(summary = "Tạo biến thể sản phẩm mới")
    public ResponseEntity<ApiResponse<Void>> createVariant(
            @Valid @RequestBody ProductVariantRequest request) {
        productVariantService.save(null, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Tạo biến thể sản phẩm thành công", null));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Cập nhật biến thể sản phẩm")
    public ResponseEntity<ApiResponse<Void>> updateVariant(
            @Parameter(description = "ID biến thể") @PathVariable Integer id,
            @Valid @RequestBody ProductVariantRequest request) {
        productVariantService.save(id, request);
        return ResponseEntity.ok(ApiResponse.success("Cập nhật biến thể sản phẩm thành công", null));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Xóa mềm biến thể sản phẩm")
    public ResponseEntity<ApiResponse<Void>> deleteVariant(
            @Parameter(description = "ID biến thể") @PathVariable Integer id) {
        productVariantService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Xóa biến thể sản phẩm thành công", null));
    }

    @PatchMapping("/{id}/restore")
    @Operation(summary = "Khôi phục biến thể sản phẩm đã xóa")
    public ResponseEntity<ApiResponse<Void>> restoreVariant(
            @Parameter(description = "ID biến thể") @PathVariable Integer id) {
        productVariantService.restore(id);
        return ResponseEntity.ok(ApiResponse.success("Khôi phục biến thể sản phẩm thành công", null));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Lấy chi tiết biến thể sản phẩm theo ID")
    public ResponseEntity<ApiResponse<ProductVariantResponse>> getVariantById(
            @Parameter(description = "ID biến thể") @PathVariable Integer id) {
        ProductVariantResponse response = productVariantService.getById(id);
        return ResponseEntity.ok(ApiResponse.success("Lấy thông tin biến thể sản phẩm thành công", response));
    }

    // ==================== LẤY DANH SÁCH ====================

    @GetMapping
    @Operation(summary = "Lấy tất cả biến thể sản phẩm (không phân trang)")
    public ResponseEntity<ApiResponse<List<ProductVariantResponse>>> getAllVariants(
            @Parameter(description = "Lọc theo status (ACTIVE/INACTIVE/OUT_OF_STOCK/DISCONTINUED)")
            @RequestParam(required = false) String status) {
        List<ProductVariantResponse> responses = productVariantService.getAll(status);
        return ResponseEntity.ok(ApiResponse.success("Lấy danh sách biến thể sản phẩm thành công", responses));
    }

    @GetMapping("/paging")
    @Operation(summary = "Lấy danh sách biến thể sản phẩm có phân trang")
    public ResponseEntity<ApiResponse<Page<ProductVariantResponse>>> getAllVariantsPaging(
            @Parameter(description = "Lọc theo status (ACTIVE/INACTIVE/OUT_OF_STOCK/DISCONTINUED)")
            @RequestParam(required = false) String status,
            
            @Parameter(description = "Số trang (bắt đầu từ 0)")
            @RequestParam(defaultValue = "0") int page,
            
            @Parameter(description = "Kích thước trang")
            @RequestParam(defaultValue = "10") int size,
            
            @Parameter(description = "Sắp xếp theo trường (ví dụ: skuVariant, price, createdAt)")
            @RequestParam(defaultValue = "createdAt") String sortBy,
            
            @Parameter(description = "Hướng sắp xếp (ASC/DESC)")
            @RequestParam(defaultValue = "DESC") String sortDirection) {
        
        Sort.Direction direction = Sort.Direction.fromString(sortDirection.toUpperCase());
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        
        Page<ProductVariantResponse> responses = productVariantService.getAllPaging(status, pageable);
        return ResponseEntity.ok(ApiResponse.success("Lấy danh sách biến thể sản phẩm thành công", responses));
    }

    // ==================== TÌM KIẾM THEO SẢN PHẨM ====================

    @GetMapping("/product/{productId}")
    @Operation(summary = "Lấy danh sách biến thể theo sản phẩm")
    public ResponseEntity<ApiResponse<List<ProductVariantResponse>>> getVariantsByProductId(
            @Parameter(description = "ID sản phẩm") @PathVariable Integer productId) {
        List<ProductVariantResponse> responses = productVariantService.getByProductId(productId);
        return ResponseEntity.ok(ApiResponse.success("Lấy danh sách biến thể theo sản phẩm thành công", responses));
    }

    @GetMapping("/product/{productId}/paging")
    @Operation(summary = "Lấy danh sách biến thể theo sản phẩm có phân trang")
    public ResponseEntity<ApiResponse<Page<ProductVariantResponse>>> getVariantsByProductIdPaging(
            @Parameter(description = "ID sản phẩm") @PathVariable Integer productId,
            
            @Parameter(description = "Số trang (bắt đầu từ 0)")
            @RequestParam(defaultValue = "0") int page,
            
            @Parameter(description = "Kích thước trang")
            @RequestParam(defaultValue = "10") int size,
            
            @Parameter(description = "Sắp xếp theo trường")
            @RequestParam(defaultValue = "createdAt") String sortBy,
            
            @Parameter(description = "Hướng sắp xếp")
            @RequestParam(defaultValue = "DESC") String sortDirection) {
        
        Sort.Direction direction = Sort.Direction.fromString(sortDirection.toUpperCase());
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        
        Page<ProductVariantResponse> responses = productVariantService.getByProductIdPaging(productId, pageable);
        return ResponseEntity.ok(ApiResponse.success("Lấy danh sách biến thể theo sản phẩm thành công", responses));
    }

    // ==================== TÌM KIẾM NÂNG CAO ====================

    @GetMapping("/search")
    @Operation(summary = "Tìm kiếm biến thể sản phẩm nâng cao")
    public ResponseEntity<ApiResponse<Page<ProductVariantResponse>>> searchVariants(
            @Parameter(description = "ID sản phẩm")
            @RequestParam(required = false) Integer productId,
            
            @Parameter(description = "ID size")
            @RequestParam(required = false) Integer sizeId,
            
            @Parameter(description = "ID màu sắc")
            @RequestParam(required = false) Integer colorId,
            
            @Parameter(description = "Từ khóa (SKU, mô tả)")
            @RequestParam(required = false) String keyword,
            
            @Parameter(description = "Giá từ")
            @RequestParam(required = false) BigDecimal minPrice,
            
            @Parameter(description = "Giá đến")
            @RequestParam(required = false) BigDecimal maxPrice,
            
            @Parameter(description = "Trạng thái")
            @RequestParam(required = false) String status,
            
            @Parameter(description = "Số trang")
            @RequestParam(defaultValue = "0") int page,
            
            @Parameter(description = "Kích thước trang")
            @RequestParam(defaultValue = "10") int size,
            
            @Parameter(description = "Sắp xếp theo trường")
            @RequestParam(defaultValue = "createdAt") String sortBy,
            
            @Parameter(description = "Hướng sắp xếp")
            @RequestParam(defaultValue = "DESC") String sortDirection) {
        
        Sort.Direction direction = Sort.Direction.fromString(sortDirection.toUpperCase());
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        
        Page<ProductVariantResponse> responses = productVariantService.searchVariants(
                productId, sizeId, colorId, keyword, minPrice, maxPrice, status, pageable);
        
        return ResponseEntity.ok(ApiResponse.success("Tìm kiếm biến thể sản phẩm thành công", responses));
    }

    @PostMapping("/search")
    @Operation(summary = "Tìm kiếm biến thể sản phẩm nâng cao (POST - dành cho nhiều tham số)")
    public ResponseEntity<ApiResponse<Page<ProductVariantResponse>>> searchVariantsPost(
            @RequestBody ProductVariantSearchRequest searchRequest,
            
            @Parameter(description = "Số trang")
            @RequestParam(defaultValue = "0") int page,
            
            @Parameter(description = "Kích thước trang")
            @RequestParam(defaultValue = "10") int size,
            
            @Parameter(description = "Sắp xếp theo trường")
            @RequestParam(required = false) String sortBy,
            
            @Parameter(description = "Hướng sắp xếp")
            @RequestParam(defaultValue = "ASC") String sortDirection) {
        
        Pageable pageable;
        if (sortBy != null && !sortBy.isEmpty()) {
            Sort.Direction direction = Sort.Direction.fromString(sortDirection.toUpperCase());
            pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        } else {
            pageable = PageRequest.of(page, size);
        }
        
        Page<ProductVariantResponse> responses = productVariantService.searchVariants(
                searchRequest.getProductId(),
                searchRequest.getSizeId(),
                searchRequest.getColorId(),
                searchRequest.getKeyword(),
                searchRequest.getMinPrice(),
                searchRequest.getMaxPrice(),
                searchRequest.getStatus(),
                pageable);
        
        return ResponseEntity.ok(ApiResponse.success("Tìm kiếm biến thể sản phẩm thành công", responses));
    }

    // ==================== KIỂM TRA TỒN TẠI ====================

    @GetMapping("/check-sku")
    @Operation(summary = "Kiểm tra mã SKU variant đã tồn tại chưa")
    public ResponseEntity<ApiResponse<Boolean>> checkSkuVariant(
            @Parameter(description = "Mã SKU variant")
            @RequestParam String skuVariant,
            
            @Parameter(description = "ID biến thể (khi cập nhật)")
            @RequestParam(required = false) Integer id) {
        
        boolean exists;
        if (id == null) {
            exists = productVariantService.existsBySkuVariant(skuVariant);
        } else {
            exists = productVariantService.existsBySkuVariantAndIdNot(skuVariant, id);
        }
        
        return ResponseEntity.ok(ApiResponse.success("Kiểm tra mã SKU variant thành công", exists));
    }

    @GetMapping("/check-combination")
    @Operation(summary = "Kiểm tra tổ hợp Product + Size + Color đã tồn tại chưa")
    public ResponseEntity<ApiResponse<Boolean>> checkCombination(
            @Parameter(description = "ID sản phẩm")
            @RequestParam Integer productId,
            
            @Parameter(description = "ID size")
            @RequestParam Integer sizeId,
            
            @Parameter(description = "ID màu sắc")
            @RequestParam Integer colorId,
            
            @Parameter(description = "ID biến thể (khi cập nhật)")
            @RequestParam(required = false) Integer id) {
        
        boolean exists = productVariantService.existsByProductAndSizeAndColor(productId, sizeId, colorId, id);
        return ResponseEntity.ok(ApiResponse.success("Kiểm tra tổ hợp thành công", exists));
    }

    // ==================== QUẢN LÝ TỒN KHO ====================

    @PatchMapping("/{id}/quantity")
    @Operation(summary = "Cập nhật số lượng tồn kho")
    public ResponseEntity<ApiResponse<Void>> updateQuantity(
            @Parameter(description = "ID biến thể") @PathVariable Integer id,
            @Parameter(description = "Số lượng mới") @RequestParam Integer quantity) {
        productVariantService.updateQuantity(id, quantity);
        return ResponseEntity.ok(ApiResponse.success("Cập nhật số lượng tồn kho thành công", null));
    }

    @PatchMapping("/{id}/decrease-quantity")
    @Operation(summary = "Giảm số lượng tồn kho")
    public ResponseEntity<ApiResponse<Void>> decreaseQuantity(
            @Parameter(description = "ID biến thể") @PathVariable Integer id,
            @Parameter(description = "Số lượng giảm") @RequestParam Integer quantity) {
        productVariantService.decreaseQuantity(id, quantity);
        return ResponseEntity.ok(ApiResponse.success("Giảm số lượng tồn kho thành công", null));
    }

    @PatchMapping("/{id}/increase-quantity")
    @Operation(summary = "Tăng số lượng tồn kho")
    public ResponseEntity<ApiResponse<Void>> increaseQuantity(
            @Parameter(description = "ID biến thể") @PathVariable Integer id,
            @Parameter(description = "Số lượng tăng") @RequestParam Integer quantity) {
        productVariantService.increaseQuantity(id, quantity);
        return ResponseEntity.ok(ApiResponse.success("Tăng số lượng tồn kho thành công", null));
    }

    @GetMapping("/{id}/check-availability")
    @Operation(summary = "Kiểm tra số lượng tồn kho có đủ không")
    public ResponseEntity<ApiResponse<Boolean>> checkAvailability(
            @Parameter(description = "ID biến thể") @PathVariable Integer id,
            @Parameter(description = "Số lượng cần kiểm tra") @RequestParam Integer quantity) {
        boolean available = productVariantService.checkAvailableQuantity(id, quantity);
        return ResponseEntity.ok(ApiResponse.success("Kiểm tra tồn kho thành công", available));
    }

    // ==================== THỐNG KÊ ====================

    @GetMapping("/statistics/count-by-product/{productId}")
    @Operation(summary = "Đếm số lượng biến thể theo sản phẩm")
    public ResponseEntity<ApiResponse<Long>> countByProductId(
            @Parameter(description = "ID sản phẩm") @PathVariable Integer productId) {
        long count = productVariantService.countByProductId(productId);
        return ResponseEntity.ok(ApiResponse.success("Đếm biến thể theo sản phẩm thành công", count));
    }

    @GetMapping("/statistics/count-by-status")
    @Operation(summary = "Đếm số lượng biến thể theo trạng thái")
    public ResponseEntity<ApiResponse<Long>> countByStatus(
            @Parameter(description = "Trạng thái (ACTIVE/INACTIVE/OUT_OF_STOCK/DISCONTINUED)")
            @RequestParam String status) {
        long count = productVariantService.countByStatus(status);
        return ResponseEntity.ok(ApiResponse.success("Đếm biến thể theo trạng thái thành công", count));
    }

    @GetMapping("/statistics/total-quantity/{productId}")
    @Operation(summary = "Tổng số lượng tồn kho theo sản phẩm")
    public ResponseEntity<ApiResponse<Integer>> getTotalQuantityByProductId(
            @Parameter(description = "ID sản phẩm") @PathVariable Integer productId) {
        Integer total = productVariantService.getTotalQuantityByProductId(productId);
        return ResponseEntity.ok(ApiResponse.success("Lấy tổng số lượng tồn kho thành công", total));
    }

    @GetMapping("/statistics/top-products")
    @Operation(summary = "Top sản phẩm có số lượng tồn kho nhiều nhất")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getTopProductsByQuantity(
            @Parameter(description = "Số lượng sản phẩm muốn lấy")
            @RequestParam(defaultValue = "10") int limit) {
        List<Map<String, Object>> topProducts = productVariantService.getTopProductsByQuantity(limit);
        return ResponseEntity.ok(ApiResponse.success("Lấy top sản phẩm thành công", topProducts));
    }

    @GetMapping("/statistics/summary")
    @Operation(summary = "Tổng quan thống kê biến thể sản phẩm")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getStatisticsSummary() {
        Map<String, Object> summary = Map.of(
            "totalVariants", productVariantService.getAll(null).size(),
            "activeVariants", productVariantService.countByStatus("ACTIVE"),
            "inactiveVariants", productVariantService.countByStatus("INACTIVE"),
            "outOfStockVariants", productVariantService.countByStatus("OUT_OF_STOCK"),
            "discontinuedVariants", productVariantService.countByStatus("DISCONTINUED"),
            "timestamp", LocalDateTime.now()
        );
        return ResponseEntity.ok(ApiResponse.success("Lấy thống kê tổng quan thành công", summary));
    }

    // ==================== BULK OPERATIONS ====================

    @PostMapping("/bulk/create")
    @Operation(summary = "Tạo nhiều biến thể cùng lúc")
    public ResponseEntity<ApiResponse<Void>> createBulkVariants(
            @Valid @RequestBody List<ProductVariantRequest> requests) {
        for (ProductVariantRequest request : requests) {
            productVariantService.save(null, request);
        }
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Tạo " + requests.size() + " biến thể thành công", null));
    }

    @PatchMapping("/bulk/status")
    @Operation(summary = "Cập nhật trạng thái hàng loạt")
    public ResponseEntity<ApiResponse<Void>> updateBulkStatus(
            @Parameter(description = "Danh sách ID biến thể")
            @RequestParam List<Integer> ids,
            
            @Parameter(description = "Trạng thái mới")
            @RequestParam String status) {
        
        for (Integer id : ids) {
            ProductVariantResponse variant = productVariantService.getById(id);
            ProductVariantRequest request = new ProductVariantRequest();
            // Map dữ liệu từ variant cũ sang request
            // và chỉ thay đổi status
            productVariantService.save(id, request);
        }
        
        return ResponseEntity.ok(ApiResponse.success("Cập nhật trạng thái hàng loạt thành công", null));
    }

    @DeleteMapping("/bulk/delete")
    @Operation(summary = "Xóa mềm hàng loạt")
    public ResponseEntity<ApiResponse<Void>> deleteBulkVariants(
            @Parameter(description = "Danh sách ID biến thể")
            @RequestParam List<Integer> ids) {
        
        for (Integer id : ids) {
            productVariantService.delete(id);
        }
        
        return ResponseEntity.ok(ApiResponse.success("Xóa " + ids.size() + " biến thể thành công", null));
    }
}