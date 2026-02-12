package com.example.sd_62.product.controller;

import com.example.sd_62.product.dto.request.UpdateSortOrderRequest;
import com.example.sd_62.product.dto.response.ApiResponse;
import com.example.sd_62.product.dto.response.ProductImageResponse;
import com.example.sd_62.product.service.ProductImageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/product-images")
@RequiredArgsConstructor
@Tag(name = "Product Image", description = "API quản lý ảnh sản phẩm")
public class ProductImageController {

    private final ProductImageService productImageService;

    // ==================== UPLOAD ====================

    @PostMapping(value = "/upload/{variantId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload 1 ảnh cho biến thể")
    public ResponseEntity<ApiResponse<ProductImageResponse>> uploadImage(
            @Parameter(description = "ID biến thể") @PathVariable Integer variantId,
            @Parameter(description = "File ảnh") @RequestParam("file") MultipartFile file,
            @Parameter(description = "Thứ tự (không bắt buộc)") @RequestParam(required = false) Integer sortOrder) {
        
        ProductImageResponse response = productImageService.uploadImage(variantId, file, sortOrder);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Upload ảnh thành công", response));
    }

    @PostMapping(value = "/upload-multiple/{variantId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload nhiều ảnh cùng lúc")
    public ResponseEntity<ApiResponse<List<ProductImageResponse>>> uploadMultipleImages(
            @Parameter(description = "ID biến thể") @PathVariable Integer variantId,
            @Parameter(description = "Danh sách file ảnh") @RequestParam("files") List<MultipartFile> files) {
        
        List<ProductImageResponse> responses = productImageService.uploadMultipleImages(variantId, files);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Upload " + responses.size() + " ảnh thành công", responses));
    }

    // ==================== GET ====================

    @GetMapping("/{imageId}")
    @Operation(summary = "Lấy thông tin ảnh theo ID")
    public ResponseEntity<ApiResponse<ProductImageResponse>> getImageById(
            @Parameter(description = "ID ảnh") @PathVariable Integer imageId) {
        
        ProductImageResponse response = productImageService.getImageById(imageId);
        return ResponseEntity.ok(ApiResponse.success("Lấy thông tin ảnh thành công", response));
    }

    @GetMapping("/variant/{variantId}")
    @Operation(summary = "Lấy tất cả ảnh của biến thể")
    public ResponseEntity<ApiResponse<List<ProductImageResponse>>> getImagesByVariantId(
            @Parameter(description = "ID biến thể") @PathVariable Integer variantId) {
        
        List<ProductImageResponse> responses = productImageService.getImagesByVariantId(variantId);
        return ResponseEntity.ok(ApiResponse.success("Lấy danh sách ảnh thành công", responses));
    }

    @GetMapping("/variant/{variantId}/count")
    @Operation(summary = "Đếm số ảnh của biến thể")
    public ResponseEntity<ApiResponse<Long>> countImagesByVariantId(
            @Parameter(description = "ID biến thể") @PathVariable Integer variantId) {
        
        long count = productImageService.countImagesByVariantId(variantId);
        return ResponseEntity.ok(ApiResponse.success("Đếm số ảnh thành công", count));
    }

    // ==================== UPDATE ====================

    @PutMapping("/{imageId}/sort-order")
    @Operation(summary = "Cập nhật thứ tự ảnh")
    public ResponseEntity<ApiResponse<ProductImageResponse>> updateSortOrder(
            @Parameter(description = "ID ảnh") @PathVariable Integer imageId,
            @Valid @RequestBody UpdateSortOrderRequest request) {
        
        ProductImageResponse response = productImageService.updateSortOrder(imageId, request.getSortOrder());
        return ResponseEntity.ok(ApiResponse.success("Cập nhật thứ tự ảnh thành công", response));
    }

    // ==================== DELETE ====================

    @DeleteMapping("/{imageId}")
    @Operation(summary = "Xóa 1 ảnh")
    public ResponseEntity<ApiResponse<Void>> deleteImage(
            @Parameter(description = "ID ảnh") @PathVariable Integer imageId) {
        
        productImageService.deleteImage(imageId);
        return ResponseEntity.ok(ApiResponse.success("Xóa ảnh thành công", null));
    }

    @DeleteMapping("/variant/{variantId}")
    @Operation(summary = "Xóa tất cả ảnh của biến thể")
    public ResponseEntity<ApiResponse<Void>> deleteAllImagesByVariantId(
            @Parameter(description = "ID biến thể") @PathVariable Integer variantId) {
        
        productImageService.deleteAllImagesByVariantId(variantId);
        return ResponseEntity.ok(ApiResponse.success("Xóa tất cả ảnh thành công", null));
    }

    @DeleteMapping("/batch")
    @Operation(summary = "Xóa nhiều ảnh cùng lúc")
    public ResponseEntity<ApiResponse<Void>> deleteImagesByIds(
            @Parameter(description = "Danh sách ID ảnh") @RequestParam List<Integer> ids) {
        
        productImageService.deleteImagesByIds(ids);
        return ResponseEntity.ok(ApiResponse.success("Xóa " + ids.size() + " ảnh thành công", null));
    }

    // ==================== VALIDATE ====================

    @GetMapping("/validate")
    @Operation(summary = "Kiểm tra ảnh có thuộc biến thể không")
    public ResponseEntity<ApiResponse<Boolean>> validateImageOwnership(
            @Parameter(description = "ID ảnh") @RequestParam Integer imageId,
            @Parameter(description = "ID biến thể") @RequestParam Integer variantId) {
        
        boolean isValid = productImageService.isImageBelongToVariant(imageId, variantId);
        return ResponseEntity.ok(ApiResponse.success("Kiểm tra thành công", isValid));
    }
}