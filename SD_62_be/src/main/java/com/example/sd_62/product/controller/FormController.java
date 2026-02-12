package com.example.sd_62.product.controller;

import com.example.sd_62.product.dto.request.FormRequest;
import com.example.sd_62.product.dto.response.ApiResponse;
import com.example.sd_62.product.dto.response.FormResponse;
import com.example.sd_62.product.service.FormService;
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
@RequestMapping("/api/forms")
@RequiredArgsConstructor
@Tag(name = "Form", description = "API quản lý form/dáng sản phẩm")
public class FormController {

    private final FormService formService;

    // ==================== CRUD CƠ BẢN ====================

    @PostMapping
    @Operation(summary = "Tạo form sản phẩm mới")
    public ResponseEntity<ApiResponse<Void>> createForm(@Valid @RequestBody FormRequest request) {
        formService.save(null, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Tạo form sản phẩm thành công", null));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Cập nhật form sản phẩm")
    public ResponseEntity<ApiResponse<Void>> updateForm(
            @Parameter(description = "ID form sản phẩm") @PathVariable Integer id,
            @Valid @RequestBody FormRequest request) {
        formService.save(id, request);
        return ResponseEntity.ok(ApiResponse.success("Cập nhật form sản phẩm thành công", null));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Xóa form sản phẩm")
    public ResponseEntity<ApiResponse<Void>> deleteForm(
            @Parameter(description = "ID form sản phẩm") @PathVariable Integer id) {
        formService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Xóa form sản phẩm thành công", null));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Lấy chi tiết form sản phẩm theo ID")
    public ResponseEntity<ApiResponse<FormResponse>> getFormById(
            @Parameter(description = "ID form sản phẩm") @PathVariable Integer id) {
        FormResponse response = formService.getById(id);
        return ResponseEntity.ok(ApiResponse.success("Lấy thông tin form sản phẩm thành công", response));
    }

    @GetMapping
    @Operation(summary = "Lấy tất cả form sản phẩm")
    public ResponseEntity<ApiResponse<List<FormResponse>>> getAllForms() {
        List<FormResponse> responses = formService.getAll();
        return ResponseEntity.ok(ApiResponse.success("Lấy danh sách form sản phẩm thành công", responses));
    }

    // ==================== TÌM KIẾM CƠ BẢN ====================

    @GetMapping("/name/{name}")
    @Operation(summary = "Tìm form sản phẩm theo tên chính xác")
    public ResponseEntity<ApiResponse<FormResponse>> getFormByName(
            @Parameter(description = "Tên form sản phẩm") @PathVariable String name) {
        FormResponse response = formService.getByName(name);
        return ResponseEntity.ok(ApiResponse.success("Tìm form sản phẩm theo tên thành công", response));
    }

    @GetMapping("/search")
    @Operation(summary = "Tìm kiếm form sản phẩm theo tên (gần đúng)")
    public ResponseEntity<ApiResponse<List<FormResponse>>> searchForms(
            @Parameter(description = "Từ khóa tìm kiếm") @RequestParam(required = false) String keyword) {
        List<FormResponse> responses = formService.searchByName(keyword);
        return ResponseEntity.ok(ApiResponse.success("Tìm kiếm form sản phẩm thành công", responses));
    }

    // ==================== KIỂM TRA ====================

    @GetMapping("/check-name")
    @Operation(summary = "Kiểm tra tên form sản phẩm đã tồn tại chưa")
    public ResponseEntity<ApiResponse<Boolean>> checkName(
            @Parameter(description = "Tên form sản phẩm") @RequestParam String name,
            @Parameter(description = "ID form sản phẩm (khi cập nhật)") @RequestParam(required = false) Integer id) {
        
        boolean exists;
        if (id == null) {
            exists = formService.existsByName(name);
        } else {
            exists = formService.existsByNameAndIdNot(name, id);
        }
        
        return ResponseEntity.ok(ApiResponse.success("Kiểm tra tên form sản phẩm thành công", exists));
    }
}