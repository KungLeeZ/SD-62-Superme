package com.example.sd_62.product.controller;

import com.example.sd_62.product.dto.request.SeasonRequest;
import com.example.sd_62.product.dto.response.ApiResponse;
import com.example.sd_62.product.dto.response.SeasonResponse;
import com.example.sd_62.product.service.SeasonService;
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
@RequestMapping("/api/seasons")
@RequiredArgsConstructor
@Tag(name = "Season", description = "API quản lý mùa/thời gian")
public class SeasonController {

    private final SeasonService seasonService;

    // ==================== CRUD CƠ BẢN ====================

    @PostMapping
    @Operation(summary = "Tạo mùa mới")
    public ResponseEntity<ApiResponse<Void>> createSeason(@Valid @RequestBody SeasonRequest request) {
        seasonService.save(null, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Tạo mùa thành công", null));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Cập nhật mùa")
    public ResponseEntity<ApiResponse<Void>> updateSeason(
            @Parameter(description = "ID mùa") @PathVariable Integer id,
            @Valid @RequestBody SeasonRequest request) {
        seasonService.save(id, request);
        return ResponseEntity.ok(ApiResponse.success("Cập nhật mùa thành công", null));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Xóa mùa")
    public ResponseEntity<ApiResponse<Void>> deleteSeason(
            @Parameter(description = "ID mùa") @PathVariable Integer id) {
        seasonService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Xóa mùa thành công", null));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Lấy chi tiết mùa theo ID")
    public ResponseEntity<ApiResponse<SeasonResponse>> getSeasonById(
            @Parameter(description = "ID mùa") @PathVariable Integer id) {
        SeasonResponse response = seasonService.getById(id);
        return ResponseEntity.ok(ApiResponse.success("Lấy thông tin mùa thành công", response));
    }

    @GetMapping
    @Operation(summary = "Lấy tất cả mùa")
    public ResponseEntity<ApiResponse<List<SeasonResponse>>> getAllSeasons() {
        List<SeasonResponse> responses = seasonService.getAll();
        return ResponseEntity.ok(ApiResponse.success("Lấy danh sách mùa thành công", responses));
    }

    @GetMapping("/paging")
    @Operation(summary = "Lấy danh sách mùa có phân trang")
    public ResponseEntity<ApiResponse<Page<SeasonResponse>>> getAllSeasonsPaging(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "year") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDirection) {
        
        Sort.Direction direction = Sort.Direction.fromString(sortDirection.toUpperCase());
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        
        Page<SeasonResponse> responses = seasonService.getAllPaging(pageable);
        return ResponseEntity.ok(ApiResponse.success("Lấy danh sách mùa thành công", responses));
    }

    // ==================== TÌM KIẾM ====================

    @GetMapping("/code/{code}")
    @Operation(summary = "Tìm mùa theo mã")
    public ResponseEntity<ApiResponse<SeasonResponse>> getBySeasonCode(
            @Parameter(description = "Mã mùa") @PathVariable String code) {
        SeasonResponse response = seasonService.getBySeasonCode(code);
        return ResponseEntity.ok(ApiResponse.success("Tìm mùa theo mã thành công", response));
    }

    @GetMapping("/name/{name}")
    @Operation(summary = "Tìm mùa theo tên")
    public ResponseEntity<ApiResponse<SeasonResponse>> getByName(
            @Parameter(description = "Tên mùa") @PathVariable String name) {
        SeasonResponse response = seasonService.getByName(name);
        return ResponseEntity.ok(ApiResponse.success("Tìm mùa theo tên thành công", response));
    }

    @GetMapping("/year/{year}")
    @Operation(summary = "Tìm mùa theo năm")
    public ResponseEntity<ApiResponse<List<SeasonResponse>>> getByYear(
            @Parameter(description = "Năm") @PathVariable Integer year) {
        List<SeasonResponse> responses = seasonService.getByYear(year);
        return ResponseEntity.ok(ApiResponse.success("Tìm mùa theo năm thành công", responses));
    }

    @GetMapping("/year-range")
    @Operation(summary = "Tìm mùa trong khoảng năm")
    public ResponseEntity<ApiResponse<List<SeasonResponse>>> getByYearRange(
            @RequestParam Integer startYear,
            @RequestParam Integer endYear) {
        List<SeasonResponse> responses = seasonService.getByYearRange(startYear, endYear);
        return ResponseEntity.ok(ApiResponse.success("Tìm mùa theo khoảng năm thành công", responses));
    }

    @GetMapping("/search")
    @Operation(summary = "Tìm kiếm mùa")
    public ResponseEntity<ApiResponse<List<SeasonResponse>>> searchSeasons(
            @Parameter(description = "Từ khóa") @RequestParam(required = false) String keyword) {
        List<SeasonResponse> responses = seasonService.search(keyword);
        return ResponseEntity.ok(ApiResponse.success("Tìm kiếm mùa thành công", responses));
    }

    @GetMapping("/search/paging")
    @Operation(summary = "Tìm kiếm mùa có phân trang")
    public ResponseEntity<ApiResponse<Page<SeasonResponse>>> searchSeasonsPaging(
            @Parameter(description = "Từ khóa") @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<SeasonResponse> responses = seasonService.searchPaging(keyword, pageable);
        return ResponseEntity.ok(ApiResponse.success("Tìm kiếm mùa thành công", responses));
    }

    // ==================== KIỂM TRA ====================

    @GetMapping("/check-code")
    @Operation(summary = "Kiểm tra mã mùa")
    public ResponseEntity<ApiResponse<Boolean>> checkCode(
            @RequestParam String seasonCode,
            @RequestParam(required = false) Integer id) {
        boolean exists = id == null 
                ? seasonService.existsBySeasonCode(seasonCode)
                : seasonService.existsBySeasonCodeAndIdNot(seasonCode, id);
        return ResponseEntity.ok(ApiResponse.success("Kiểm tra mã mùa thành công", exists));
    }

    @GetMapping("/check-name")
    @Operation(summary = "Kiểm tra tên mùa")
    public ResponseEntity<ApiResponse<Boolean>> checkName(
            @RequestParam String name,
            @RequestParam(required = false) Integer id) {
        boolean exists = id == null 
                ? seasonService.existsByName(name)
                : seasonService.existsByNameAndIdNot(name, id);
        return ResponseEntity.ok(ApiResponse.success("Kiểm tra tên mùa thành công", exists));
    }

    // ==================== THỐNG KÊ ====================

    @GetMapping("/statistics/count-by-year/{year}")
    @Operation(summary = "Đếm số mùa trong năm")
    public ResponseEntity<ApiResponse<Long>> countByYear(@PathVariable Integer year) {
        long count = seasonService.countByYear(year);
        return ResponseEntity.ok(ApiResponse.success("Đếm số mùa theo năm thành công", count));
    }
}