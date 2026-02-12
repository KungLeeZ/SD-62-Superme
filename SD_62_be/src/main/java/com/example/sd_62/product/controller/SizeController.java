package com.example.sd_62.product.controller;

import com.example.sd_62.product.dto.response.ApiResponse;
import com.example.sd_62.product.dto.response.SizeResponse;
import com.example.sd_62.product.service.SizeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/sizes")
@RequiredArgsConstructor
@Tag(name = "Size", description = "API danh mục size giày (CỐ ĐỊNH)")
public class SizeController {

    private final SizeService sizeService;

    @GetMapping
    @Operation(summary = "Lấy tất cả sizes")
    public ResponseEntity<ApiResponse<List<SizeResponse>>> getAllSizes() {
        List<SizeResponse> responses = sizeService.getAll();
        return ResponseEntity.ok(ApiResponse.success("Lấy danh sách size thành công", responses));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Lấy size theo ID")
    public ResponseEntity<ApiResponse<SizeResponse>> getSizeById(
            @Parameter(description = "ID size") @PathVariable Integer id) {
        SizeResponse response = sizeService.getById(id);
        return ResponseEntity.ok(ApiResponse.success("Lấy thông tin size thành công", response));
    }

    @GetMapping("/jp/{value}")
    @Operation(summary = "Tìm size theo JP")
    public ResponseEntity<ApiResponse<SizeResponse>> getByJp(
            @Parameter(description = "Size JP (ví dụ: 25.0)") @PathVariable BigDecimal value) {
        SizeResponse response = sizeService.getByJp(value);
        return ResponseEntity.ok(ApiResponse.success("Tìm size theo JP thành công", response));
    }

    @GetMapping("/eu/{value}")
    @Operation(summary = "Tìm size theo EU")
    public ResponseEntity<ApiResponse<SizeResponse>> getByEu(
            @Parameter(description = "Size EU (ví dụ: 40)") @PathVariable BigDecimal value) {
        SizeResponse response = sizeService.getByEu(value);
        return ResponseEntity.ok(ApiResponse.success("Tìm size theo EU thành công", response));
    }

    @GetMapping("/us-men/{value}")
    @Operation(summary = "Tìm size theo US Men")
    public ResponseEntity<ApiResponse<SizeResponse>> getByUsMen(
            @Parameter(description = "Size US Men (ví dụ: 7.0)") @PathVariable BigDecimal value) {
        SizeResponse response = sizeService.getByUsMen(value);
        return ResponseEntity.ok(ApiResponse.success("Tìm size theo US Men thành công", response));
    }

    @GetMapping("/us-women/{value}")
    @Operation(summary = "Tìm size theo US Women")
    public ResponseEntity<ApiResponse<SizeResponse>> getByUsWomen(
            @Parameter(description = "Size US Women (ví dụ: 8.0)") @PathVariable BigDecimal value) {
        SizeResponse response = sizeService.getByUsWomen(value);
        return ResponseEntity.ok(ApiResponse.success("Tìm size theo US Women thành công", response));
    }

    @GetMapping("/foot-length/{value}")
    @Operation(summary = "Tìm size theo chiều dài chân")
    public ResponseEntity<ApiResponse<SizeResponse>> getByFootLength(
            @Parameter(description = "Chiều dài chân (cm)") @PathVariable BigDecimal value) {
        SizeResponse response = sizeService.getByFootLength(value);
        return ResponseEntity.ok(ApiResponse.success("Tìm size theo chiều dài chân thành công", response));
    }

    @GetMapping("/range")
    @Operation(summary = "Tìm size trong khoảng JP")
    public ResponseEntity<ApiResponse<List<SizeResponse>>> getByJpRange(
            @Parameter(description = "Từ size JP") @RequestParam BigDecimal from,
            @Parameter(description = "Đến size JP") @RequestParam BigDecimal to) {
        List<SizeResponse> responses = sizeService.getByJpRange(from, to);
        return ResponseEntity.ok(ApiResponse.success("Tìm size theo khoảng thành công", responses));
    }
}