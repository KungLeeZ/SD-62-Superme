package com.example.sd_62.product.controller;

import com.example.sd_62.product.dto.request.MaterialRequest;
import com.example.sd_62.product.dto.response.ApiResponse;
import com.example.sd_62.product.dto.response.MaterialResponse;
import com.example.sd_62.product.service.MaterialService;
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
@RequestMapping("/api/materials")
@RequiredArgsConstructor
@Tag(name = "Material", description = "API quản lý chất liệu")
public class MaterialController {

    private final MaterialService materialService;

    // ==================== CRUD CƠ BẢN ====================

    @PostMapping
    @Operation(summary = "Tạo chất liệu mới")
    public ResponseEntity<ApiResponse<Void>> createMaterial(@Valid @RequestBody MaterialRequest request) {
        materialService.save(null, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Tạo chất liệu thành công", null));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Cập nhật chất liệu")
    public ResponseEntity<ApiResponse<Void>> updateMaterial(
            @Parameter(description = "ID chất liệu") @PathVariable Integer id,
            @Valid @RequestBody MaterialRequest request) {
        materialService.save(id, request);
        return ResponseEntity.ok(ApiResponse.success("Cập nhật chất liệu thành công", null));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Xóa chất liệu")
    public ResponseEntity<ApiResponse<Void>> deleteMaterial(
            @Parameter(description = "ID chất liệu") @PathVariable Integer id) {
        materialService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Xóa chất liệu thành công", null));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Lấy chi tiết chất liệu theo ID")
    public ResponseEntity<ApiResponse<MaterialResponse>> getMaterialById(
            @Parameter(description = "ID chất liệu") @PathVariable Integer id) {
        MaterialResponse response = materialService.getById(id);
        return ResponseEntity.ok(ApiResponse.success("Lấy thông tin chất liệu thành công", response));
    }

    @GetMapping
    @Operation(summary = "Lấy tất cả chất liệu")
    public ResponseEntity<ApiResponse<List<MaterialResponse>>> getAllMaterials() {
        List<MaterialResponse> responses = materialService.getAll();
        return ResponseEntity.ok(ApiResponse.success("Lấy danh sách chất liệu thành công", responses));
    }

    // ==================== TÌM KIẾM CƠ BẢN ====================

    @GetMapping("/name/{name}")
    @Operation(summary = "Tìm chất liệu theo tên chính xác")
    public ResponseEntity<ApiResponse<MaterialResponse>> getMaterialByName(
            @Parameter(description = "Tên chất liệu") @PathVariable String name) {
        MaterialResponse response = materialService.getByName(name);
        return ResponseEntity.ok(ApiResponse.success("Tìm chất liệu theo tên thành công", response));
    }

    @GetMapping("/search")
    @Operation(summary = "Tìm kiếm chất liệu theo tên (gần đúng)")
    public ResponseEntity<ApiResponse<List<MaterialResponse>>> searchMaterials(
            @Parameter(description = "Từ khóa tìm kiếm") @RequestParam(required = false) String keyword) {
        List<MaterialResponse> responses = materialService.searchByName(keyword);
        return ResponseEntity.ok(ApiResponse.success("Tìm kiếm chất liệu thành công", responses));
    }

    // ==================== KIỂM TRA ====================

    @GetMapping("/check-name")
    @Operation(summary = "Kiểm tra tên chất liệu đã tồn tại chưa")
    public ResponseEntity<ApiResponse<Boolean>> checkName(
            @Parameter(description = "Tên chất liệu") @RequestParam String name,
            @Parameter(description = "ID chất liệu (khi cập nhật)") @RequestParam(required = false) Integer id) {
        
        boolean exists;
        if (id == null) {
            exists = materialService.existsByName(name);
        } else {
            exists = materialService.existsByNameAndIdNot(name, id);
        }
        
        return ResponseEntity.ok(ApiResponse.success("Kiểm tra tên chất liệu thành công", exists));
    }
}