package com.example.sd_62.product.controller;

import com.example.sd_62.product.dto.request.ProductReleaseRequest;
import com.example.sd_62.product.dto.response.ApiResponse;
import com.example.sd_62.product.dto.response.ProductReleaseResponse;
import com.example.sd_62.product.service.ProductReleaseService;
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

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/product-releases")
@RequiredArgsConstructor
@Tag(name = "Product Release", description = "API quản lý đợt phát hành sản phẩm")
public class ProductReleaseController {

    private final ProductReleaseService productReleaseService;

    // ==================== CRUD CƠ BẢN ====================

    @PostMapping
    @Operation(summary = "Tạo đợt phát hành mới")
    public ResponseEntity<ApiResponse<Void>> createRelease(@Valid @RequestBody ProductReleaseRequest request) {
        productReleaseService.save(null, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Tạo đợt phát hành thành công", null));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Cập nhật đợt phát hành")
    public ResponseEntity<ApiResponse<Void>> updateRelease(
            @Parameter(description = "ID đợt phát hành") @PathVariable Integer id,
            @Valid @RequestBody ProductReleaseRequest request) {
        productReleaseService.save(id, request);
        return ResponseEntity.ok(ApiResponse.success("Cập nhật đợt phát hành thành công", null));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Xóa mềm đợt phát hành")
    public ResponseEntity<ApiResponse<Void>> deleteRelease(
            @Parameter(description = "ID đợt phát hành") @PathVariable Integer id) {
        productReleaseService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Xóa đợt phát hành thành công", null));
    }

    @PatchMapping("/{id}/restore")
    @Operation(summary = "Khôi phục đợt phát hành")
    public ResponseEntity<ApiResponse<Void>> restoreRelease(
            @Parameter(description = "ID đợt phát hành") @PathVariable Integer id) {
        productReleaseService.restore(id);
        return ResponseEntity.ok(ApiResponse.success("Khôi phục đợt phát hành thành công", null));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Lấy chi tiết đợt phát hành theo ID")
    public ResponseEntity<ApiResponse<ProductReleaseResponse>> getReleaseById(
            @Parameter(description = "ID đợt phát hành") @PathVariable Integer id) {
        ProductReleaseResponse response = productReleaseService.getById(id);
        return ResponseEntity.ok(ApiResponse.success("Lấy thông tin đợt phát hành thành công", response));
    }

    @GetMapping
    @Operation(summary = "Lấy tất cả đợt phát hành")
    public ResponseEntity<ApiResponse<List<ProductReleaseResponse>>> getAllReleases(
            @Parameter(description = "Lọc theo status (ACTIVE/INACTIVE)")
            @RequestParam(required = false) String status) {
        List<ProductReleaseResponse> responses = productReleaseService.getAll(status);
        return ResponseEntity.ok(ApiResponse.success("Lấy danh sách đợt phát hành thành công", responses));
    }

    @GetMapping("/paging")
    @Operation(summary = "Lấy danh sách đợt phát hành có phân trang")
    public ResponseEntity<ApiResponse<Page<ProductReleaseResponse>>> getAllReleasesPaging(
            @Parameter(description = "Lọc theo status") @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "startDate") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDirection) {
        
        Sort.Direction direction = Sort.Direction.fromString(sortDirection.toUpperCase());
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        
        Page<ProductReleaseResponse> responses = productReleaseService.getAllPaging(status, pageable);
        return ResponseEntity.ok(ApiResponse.success("Lấy danh sách đợt phát hành thành công", responses));
    }

    // ==================== TÌM THEO MÙA ====================

    @GetMapping("/season/{seasonId}")
    @Operation(summary = "Lấy đợt phát hành theo mùa")
    public ResponseEntity<ApiResponse<List<ProductReleaseResponse>>> getBySeasonId(
            @Parameter(description = "ID mùa") @PathVariable Integer seasonId) {
        List<ProductReleaseResponse> responses = productReleaseService.getBySeasonId(seasonId);
        return ResponseEntity.ok(ApiResponse.success("Lấy đợt phát hành theo mùa thành công", responses));
    }

    // ==================== TÌM THEO THỜI GIAN ====================

    @GetMapping("/current")
    @Operation(summary = "Lấy đợt phát hành đang diễn ra")
    public ResponseEntity<ApiResponse<List<ProductReleaseResponse>>> getCurrentReleases() {
        List<ProductReleaseResponse> responses = productReleaseService.getCurrentReleases();
        return ResponseEntity.ok(ApiResponse.success("Lấy đợt phát hành đang diễn ra thành công", responses));
    }

    @GetMapping("/date-range")
    @Operation(summary = "Lấy đợt phát hành trong khoảng thời gian")
    public ResponseEntity<ApiResponse<List<ProductReleaseResponse>>> getByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to) {
        List<ProductReleaseResponse> responses = productReleaseService.getByDateRange(from, to);
        return ResponseEntity.ok(ApiResponse.success("Lấy đợt phát hành theo khoảng thời gian thành công", responses));
    }

    // ==================== TÌM KIẾM ====================

    @GetMapping("/search")
    @Operation(summary = "Tìm kiếm đợt phát hành")
    public ResponseEntity<ApiResponse<List<ProductReleaseResponse>>> searchReleases(
            @Parameter(description = "Từ khóa (mã hoặc tên)") @RequestParam(required = false) String keyword) {
        List<ProductReleaseResponse> responses = productReleaseService.search(keyword);
        return ResponseEntity.ok(ApiResponse.success("Tìm kiếm đợt phát hành thành công", responses));
    }

    @GetMapping("/search/paging")
    @Operation(summary = "Tìm kiếm đợt phát hành có phân trang")
    public ResponseEntity<ApiResponse<Page<ProductReleaseResponse>>> searchReleasesPaging(
            @Parameter(description = "Từ khóa") @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ProductReleaseResponse> responses = productReleaseService.searchPaging(keyword, pageable);
        return ResponseEntity.ok(ApiResponse.success("Tìm kiếm đợt phát hành thành công", responses));
    }

    // ==================== KIỂM TRA ====================

    @GetMapping("/check-code")
    @Operation(summary = "Kiểm tra mã đợt phát hành")
    public ResponseEntity<ApiResponse<Boolean>> checkCode(
            @RequestParam String code,
            @RequestParam(required = false) Integer id) {
        boolean exists = id == null 
                ? productReleaseService.existsByCode(code)
                : productReleaseService.existsByCodeAndIdNot(code, id);
        return ResponseEntity.ok(ApiResponse.success("Kiểm tra mã thành công", exists));
    }

    // ==================== THỐNG KÊ ====================

    @GetMapping("/statistics/count-by-status")
    @Operation(summary = "Đếm số lượng theo trạng thái")
    public ResponseEntity<ApiResponse<Long>> countByStatus(@RequestParam String status) {
        long count = productReleaseService.countByStatus(status);
        return ResponseEntity.ok(ApiResponse.success("Đếm thành công", count));
    }
}