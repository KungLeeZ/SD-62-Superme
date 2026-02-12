package com.example.sd_62.product.controller;

import com.example.sd_62.product.dto.request.ColorRequest;
import com.example.sd_62.product.dto.response.ApiResponse;
import com.example.sd_62.product.dto.response.ColorResponse;
import com.example.sd_62.product.service.ColorService;
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
@RequestMapping("/api/colors")
@RequiredArgsConstructor
@Tag(name = "Color", description = "API quản lý màu sắc")
public class ColorController {

    private final ColorService colorService;

    // ==================== CRUD CƠ BẢN ====================

    @PostMapping
    @Operation(summary = "Tạo màu sắc mới")
    public ResponseEntity<ApiResponse<Void>> createColor(@Valid @RequestBody ColorRequest request) {
        colorService.save(null, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Tạo màu sắc thành công", null));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Cập nhật màu sắc")
    public ResponseEntity<ApiResponse<Void>> updateColor(
            @Parameter(description = "ID màu sắc") @PathVariable Integer id,
            @Valid @RequestBody ColorRequest request) {
        colorService.save(id, request);
        return ResponseEntity.ok(ApiResponse.success("Cập nhật màu sắc thành công", null));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Xóa màu sắc")
    public ResponseEntity<ApiResponse<Void>> deleteColor(
            @Parameter(description = "ID màu sắc") @PathVariable Integer id) {
        colorService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Xóa màu sắc thành công", null));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Lấy chi tiết màu sắc theo ID")
    public ResponseEntity<ApiResponse<ColorResponse>> getColorById(
            @Parameter(description = "ID màu sắc") @PathVariable Integer id) {
        ColorResponse response = colorService.getById(id);
        return ResponseEntity.ok(ApiResponse.success("Lấy thông tin màu sắc thành công", response));
    }

    @GetMapping
    @Operation(summary = "Lấy tất cả màu sắc")
    public ResponseEntity<ApiResponse<List<ColorResponse>>> getAllColors() {
        List<ColorResponse> responses = colorService.getAll();
        return ResponseEntity.ok(ApiResponse.success("Lấy danh sách màu sắc thành công", responses));
    }

    // ==================== TÌM KIẾM CƠ BẢN ====================

    @GetMapping("/name/{name}")
    @Operation(summary = "Tìm màu sắc theo tên")
    public ResponseEntity<ApiResponse<ColorResponse>> getColorByName(
            @Parameter(description = "Tên màu sắc") @PathVariable String name) {
        ColorResponse response = colorService.getByName(name);
        return ResponseEntity.ok(ApiResponse.success("Tìm màu sắc theo tên thành công", response));
    }

    @GetMapping("/hex/{hex}")
    @Operation(summary = "Tìm màu sắc theo mã hex")
    public ResponseEntity<ApiResponse<ColorResponse>> getColorByHex(
            @Parameter(description = "Mã hex (ví dụ: #FF0000)") @PathVariable String hex) {
        ColorResponse response = colorService.getByHex(hex);
        return ResponseEntity.ok(ApiResponse.success("Tìm màu sắc theo mã hex thành công", response));
    }

    @GetMapping("/search")
    @Operation(summary = "Tìm kiếm màu sắc theo tên")
    public ResponseEntity<ApiResponse<List<ColorResponse>>> searchColors(
            @Parameter(description = "Từ khóa tìm kiếm") @RequestParam(required = false) String keyword) {
        List<ColorResponse> responses = colorService.searchByName(keyword);
        return ResponseEntity.ok(ApiResponse.success("Tìm kiếm màu sắc thành công", responses));
    }

    // ==================== KIỂM TRA ====================

    @GetMapping("/check-name")
    @Operation(summary = "Kiểm tra tên màu đã tồn tại chưa")
    public ResponseEntity<ApiResponse<Boolean>> checkName(
            @Parameter(description = "Tên màu") @RequestParam String name,
            @Parameter(description = "ID màu (khi cập nhật)") @RequestParam(required = false) Integer id) {
        
        boolean exists;
        if (id == null) {
            exists = colorService.existsByName(name);
        } else {
            exists = colorService.existsByNameAndIdNot(name, id);
        }
        
        return ResponseEntity.ok(ApiResponse.success("Kiểm tra tên màu thành công", exists));
    }

    @GetMapping("/check-hex")
    @Operation(summary = "Kiểm tra mã hex đã tồn tại chưa")
    public ResponseEntity<ApiResponse<Boolean>> checkHex(
            @Parameter(description = "Mã hex") @RequestParam String hex,
            @Parameter(description = "ID màu (khi cập nhật)") @RequestParam(required = false) Integer id) {
        
        boolean exists;
        if (id == null) {
            exists = colorService.existsByHex(hex);
        } else {
            exists = colorService.existsByHexAndIdNot(hex, id);
        }
        
        return ResponseEntity.ok(ApiResponse.success("Kiểm tra mã hex thành công", exists));
    }
}