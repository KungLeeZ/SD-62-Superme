package com.example.sd_62.product.controller;

import com.example.sd_62.product.dto.request.BrandRequest;
import com.example.sd_62.product.dto.response.ApiResponse;
import com.example.sd_62.product.dto.response.BrandResponse;
import com.example.sd_62.product.enums.BrandStatus;
import com.example.sd_62.product.service.BrandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/brands")
@RequiredArgsConstructor
@Tag(name = "Brand", description = "API quản lý thương hiệu")
public class BrandController {

    private final BrandService brandService;

    // ==================== CRUD CƠ BẢN ====================

    @PostMapping
    @Operation(summary = "Tạo thương hiệu mới")
    public ResponseEntity<ApiResponse<Void>> createBrand(@Valid @RequestBody BrandRequest request) {
        brandService.save(null, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Tạo thương hiệu thành công", null));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Cập nhật thương hiệu")
    public ResponseEntity<ApiResponse<Void>> updateBrand(
            @Parameter(description = "ID thương hiệu") @PathVariable Integer id,
            @Valid @RequestBody BrandRequest request) {
        brandService.save(id, request);
        return ResponseEntity.ok(ApiResponse.success("Cập nhật thương hiệu thành công", null));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Xóa mềm thương hiệu")
    public ResponseEntity<ApiResponse<Void>> deleteBrand(
            @Parameter(description = "ID thương hiệu") @PathVariable Integer id) {
        brandService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Xóa thương hiệu thành công", null));
    }

    @PatchMapping("/{id}/restore")
    @Operation(summary = "Khôi phục thương hiệu đã xóa")
    public ResponseEntity<ApiResponse<Void>> restoreBrand(
            @Parameter(description = "ID thương hiệu") @PathVariable Integer id) {
        brandService.restore(id);
        return ResponseEntity.ok(ApiResponse.success("Khôi phục thương hiệu thành công", null));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Lấy chi tiết thương hiệu theo ID")
    public ResponseEntity<ApiResponse<BrandResponse>> getBrandById(
            @Parameter(description = "ID thương hiệu") @PathVariable Integer id) {
        BrandResponse response = brandService.getById(id);
        return ResponseEntity.ok(ApiResponse.success("Lấy thông tin thương hiệu thành công", response));
    }

    @GetMapping
    @Operation(summary = "Lấy tất cả thương hiệu")
    public ResponseEntity<ApiResponse<List<BrandResponse>>> getAllBrands(
            @Parameter(description = "Lọc theo status (ACTIVE/INACTIVE)")
            @RequestParam(required = false) String status) {
        List<BrandResponse> responses = brandService.getAll(status);
        return ResponseEntity.ok(ApiResponse.success("Lấy danh sách thương hiệu thành công", responses));
    }

    // ==================== TÌM KIẾM CƠ BẢN ====================

    @GetMapping("/code/{code}")
    @Operation(summary = "Tìm thương hiệu theo mã")
    public ResponseEntity<ApiResponse<BrandResponse>> getBrandByCode(
            @Parameter(description = "Mã thương hiệu") @PathVariable String code) {
        BrandResponse response = brandService.getByCode(code);
        return ResponseEntity.ok(ApiResponse.success("Tìm thương hiệu theo mã thành công", response));
    }

    @GetMapping("/name/{name}")
    @Operation(summary = "Tìm thương hiệu theo tên")
    public ResponseEntity<ApiResponse<BrandResponse>> getBrandByName(
            @Parameter(description = "Tên thương hiệu") @PathVariable String name) {
        BrandResponse response = brandService.getByName(name);
        return ResponseEntity.ok(ApiResponse.success("Tìm thương hiệu theo tên thành công", response));
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Lấy danh sách thương hiệu theo trạng thái")
    public ResponseEntity<ApiResponse<List<BrandResponse>>> getBrandsByStatus(
            @Parameter(description = "Trạng thái (ACTIVE/INACTIVE)") @PathVariable BrandStatus status) {
        List<BrandResponse> responses = brandService.getByStatus(status);
        return ResponseEntity.ok(ApiResponse.success("Lấy danh sách thương hiệu theo trạng thái thành công", responses));
    }

    // ==================== KIỂM TRA ====================

    @GetMapping("/check-code")
    @Operation(summary = "Kiểm tra mã thương hiệu đã tồn tại chưa")
    public ResponseEntity<ApiResponse<Boolean>> checkCode(
            @Parameter(description = "Mã thương hiệu") @RequestParam String code,
            @Parameter(description = "ID thương hiệu (khi cập nhật)") @RequestParam(required = false) Integer id) {
        
        boolean exists;
        if (id == null) {
            exists = brandService.existsByCode(code);
        } else {
            exists = brandService.existsByCodeAndIdNot(code, id);
        }
        
        return ResponseEntity.ok(ApiResponse.success("Kiểm tra mã thương hiệu thành công", exists));
    }

    @GetMapping("/check-name")
    @Operation(summary = "Kiểm tra tên thương hiệu đã tồn tại chưa")
    public ResponseEntity<ApiResponse<Boolean>> checkName(
            @Parameter(description = "Tên thương hiệu") @RequestParam String name,
            @Parameter(description = "ID thương hiệu (khi cập nhật)") @RequestParam(required = false) Integer id) {
        
        boolean exists;
        if (id == null) {
            exists = brandService.existsByName(name);
        } else {
            exists = brandService.existsByNameAndIdNot(name, id);
        }
        
        return ResponseEntity.ok(ApiResponse.success("Kiểm tra tên thương hiệu thành công", exists));
    }

    // ==================== THỐNG KÊ ====================

    @GetMapping("/count")
    @Operation(summary = "Đếm số lượng thương hiệu theo trạng thái")
    public ResponseEntity<ApiResponse<Long>> countByStatus(
            @Parameter(description = "Trạng thái (ACTIVE/INACTIVE)") @RequestParam String status) {
        long count = brandService.countByStatus(status);
        return ResponseEntity.ok(ApiResponse.success("Đếm thương hiệu theo trạng thái thành công", count));
    }

    @GetMapping("/statistics/summary")
    @Operation(summary = "Tổng quan thống kê thương hiệu")
    public ResponseEntity<ApiResponse<Object>> getStatisticsSummary() {
        long total = brandService.getAll(null).size();
        long active = brandService.countByStatus("ACTIVE");
        long inactive = brandService.countByStatus("INACTIVE");
        
        Object summary = Map.of(
            "totalBrands", total,
            "activeBrands", active,
            "inactiveBrands", inactive
        );
        
        return ResponseEntity.ok(ApiResponse.success("Lấy thống kê tổng quan thành công", summary));
    }
}