package com.example.sd_62.product.controller;

import com.example.sd_62.product.dto.request.CollabRequest;
import com.example.sd_62.product.dto.response.ApiResponse;
import com.example.sd_62.product.dto.response.CollabResponse;
import com.example.sd_62.product.service.CollabService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/collabs")
@RequiredArgsConstructor
@Tag(name = "Collab", description = "API quản lý collab/hợp tác")
public class CollabController {

    private final CollabService collabService;

    // ===== CRUD CƠ BẢN =====

    @PostMapping
    @Operation(summary = "Tạo collab mới")
    public ResponseEntity<ApiResponse<Void>> createCollab(@Valid @RequestBody CollabRequest request) {
        collabService.save(null, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Tạo collab thành công", null));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Cập nhật collab")
    public ResponseEntity<ApiResponse<Void>> updateCollab(
            @Parameter(description = "ID collab") @PathVariable Integer id,
            @Valid @RequestBody CollabRequest request) {
        collabService.save(id, request);
        return ResponseEntity.ok(ApiResponse.success("Cập nhật collab thành công", null));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Xóa collab")
    public ResponseEntity<ApiResponse<Void>> deleteCollab(
            @Parameter(description = "ID collab") @PathVariable Integer id) {
        collabService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Xóa collab thành công", null));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Lấy chi tiết collab theo ID")
    public ResponseEntity<ApiResponse<CollabResponse>> getCollabById(
            @Parameter(description = "ID collab") @PathVariable Integer id) {
        CollabResponse response = collabService.getById(id);
        return ResponseEntity.ok(ApiResponse.success("Lấy thông tin collab thành công", response));
    }

    @GetMapping
    @Operation(summary = "Lấy tất cả collab")
    public ResponseEntity<ApiResponse<List<CollabResponse>>> getAllCollabs() {
        List<CollabResponse> responses = collabService.getAll();
        return ResponseEntity.ok(ApiResponse.success("Lấy danh sách collab thành công", responses));
    }

    // ===== TÌM KIẾM CƠ BẢN =====

    @GetMapping("/name/{name}")
    @Operation(summary = "Tìm collab theo tên")
    public ResponseEntity<ApiResponse<CollabResponse>> getCollabByName(
            @Parameter(description = "Tên collab") @PathVariable String name) {
        CollabResponse response = collabService.getByName(name);
        return ResponseEntity.ok(ApiResponse.success("Tìm collab theo tên thành công", response));
    }

    @GetMapping("/type/{type}")
    @Operation(summary = "Tìm collab theo type")
    public ResponseEntity<ApiResponse<List<CollabResponse>>> getCollabsByType(
            @Parameter(description = "Type collab") @PathVariable String type) {
        List<CollabResponse> responses = collabService.getByType(type);
        return ResponseEntity.ok(ApiResponse.success("Tìm collab theo type thành công", responses));
    }

    @GetMapping("/year/{year}")
    @Operation(summary = "Tìm collab theo năm")
    public ResponseEntity<ApiResponse<List<CollabResponse>>> getCollabsByYear(
            @Parameter(description = "Năm") @PathVariable Integer year) {
        List<CollabResponse> responses = collabService.getByYear(year);
        return ResponseEntity.ok(ApiResponse.success("Tìm collab theo năm thành công", responses));
    }

    @GetMapping("/year-range")
    @Operation(summary = "Tìm collab trong khoảng năm")
    public ResponseEntity<ApiResponse<List<CollabResponse>>> getCollabsByYearRange(
            @Parameter(description = "Năm bắt đầu") @RequestParam Integer startYear,
            @Parameter(description = "Năm kết thúc") @RequestParam Integer endYear) {
        List<CollabResponse> responses = collabService.getByYearRange(startYear, endYear);
        return ResponseEntity.ok(ApiResponse.success("Tìm collab theo khoảng năm thành công", responses));
    }

    @GetMapping("/search")
    @Operation(summary = "Tìm collab theo type và năm")
    public ResponseEntity<ApiResponse<List<CollabResponse>>> getCollabsByTypeAndYear(
            @Parameter(description = "Type collab") @RequestParam String type,
            @Parameter(description = "Năm") @RequestParam Integer year) {
        List<CollabResponse> responses = collabService.getByTypeAndYear(type, year);
        return ResponseEntity.ok(ApiResponse.success("Tìm collab theo type và năm thành công", responses));
    }

    // ===== KIỂM TRA =====

    @GetMapping("/check-name")
    @Operation(summary = "Kiểm tra tên collab đã tồn tại chưa")
    public ResponseEntity<ApiResponse<Boolean>> checkName(
            @Parameter(description = "Tên collab") @RequestParam String name,
            @Parameter(description = "ID collab (khi cập nhật)") @RequestParam(required = false) Integer id) {
        
        boolean exists;
        if (id == null) {
            exists = collabService.existsByName(name);
        } else {
            exists = collabService.existsByNameAndIdNot(name, id);
        }
        
        return ResponseEntity.ok(ApiResponse.success("Kiểm tra tên collab thành công", exists));
    }
}