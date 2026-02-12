package com.example.sd_62.product.controller;

import com.example.sd_62.product.dto.request.ProductCollabRequest;
import com.example.sd_62.product.dto.request.SyncCollabsRequest;
import com.example.sd_62.product.dto.response.ApiResponse;
import com.example.sd_62.product.dto.response.CollabResponse;
import com.example.sd_62.product.dto.response.ProductCollabResponse;
import com.example.sd_62.product.dto.response.ProductResponse;
import com.example.sd_62.product.service.ProductCollabService;
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
@RequestMapping("/api/product-collabs")
@RequiredArgsConstructor
@Tag(name = "Product Collab", description = "API quản lý collab cho sản phẩm")
public class ProductCollabController {

    private final ProductCollabService productCollabService;

    // ==================== CRUD CƠ BẢN ====================

    @PostMapping
    @Operation(summary = "Gán collab cho sản phẩm")
    public ResponseEntity<ApiResponse<ProductCollabResponse>> createProductCollab(
            @Valid @RequestBody ProductCollabRequest request) {
        ProductCollabResponse response = productCollabService.save(null, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Gán collab cho sản phẩm thành công", response));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Xóa collab khỏi sản phẩm")
    public ResponseEntity<ApiResponse<Void>> deleteProductCollab(
            @Parameter(description = "ID product collab") @PathVariable Integer id) {
        productCollabService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Xóa collab khỏi sản phẩm thành công", null));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Lấy thông tin product collab theo ID")
    public ResponseEntity<ApiResponse<ProductCollabResponse>> getProductCollabById(
            @Parameter(description = "ID product collab") @PathVariable Integer id) {
        ProductCollabResponse response = productCollabService.getById(id);
        return ResponseEntity.ok(ApiResponse.success("Lấy thông tin thành công", response));
    }

    @GetMapping
    @Operation(summary = "Lấy tất cả product collab")
    public ResponseEntity<ApiResponse<List<ProductCollabResponse>>> getAllProductCollabs() {
        List<ProductCollabResponse> responses = productCollabService.getAll();
        return ResponseEntity.ok(ApiResponse.success("Lấy danh sách thành công", responses));
    }

    // ==================== QUẢN LÝ THEO PRODUCT ====================

    @GetMapping("/product/{productId}")
    @Operation(summary = "Lấy danh sách collab theo sản phẩm")
    public ResponseEntity<ApiResponse<List<ProductCollabResponse>>> getByProductId(
            @Parameter(description = "ID sản phẩm") @PathVariable Integer productId) {
        List<ProductCollabResponse> responses = productCollabService.getByProductId(productId);
        return ResponseEntity.ok(ApiResponse.success("Lấy danh sách collab theo sản phẩm thành công", responses));
    }

    @GetMapping("/product/{productId}/collabs")
    @Operation(summary = "Lấy danh sách collab (response) theo sản phẩm")
    public ResponseEntity<ApiResponse<List<CollabResponse>>> getCollabsByProductId(
            @Parameter(description = "ID sản phẩm") @PathVariable Integer productId) {
        List<CollabResponse> responses = productCollabService.getCollabsByProductId(productId);
        return ResponseEntity.ok(ApiResponse.success("Lấy danh sách collab theo sản phẩm thành công", responses));
    }

    @GetMapping("/product/{productId}/collab-ids")
    @Operation(summary = "Lấy danh sách ID collab theo sản phẩm")
    public ResponseEntity<ApiResponse<List<Integer>>> getCollabIdsByProductId(
            @Parameter(description = "ID sản phẩm") @PathVariable Integer productId) {
        List<Integer> responses = productCollabService.getCollabIdsByProductId(productId);
        return ResponseEntity.ok(ApiResponse.success("Lấy danh sách ID collab thành công", responses));
    }

    @DeleteMapping("/product/{productId}")
    @Operation(summary = "Xóa tất cả collab của sản phẩm")
    public ResponseEntity<ApiResponse<Void>> deleteByProductId(
            @Parameter(description = "ID sản phẩm") @PathVariable Integer productId) {
        productCollabService.deleteByProductId(productId);
        return ResponseEntity.ok(ApiResponse.success("Xóa tất cả collab của sản phẩm thành công", null));
    }

    @GetMapping("/product/{productId}/count")
    @Operation(summary = "Đếm số lượng collab của sản phẩm")
    public ResponseEntity<ApiResponse<Long>> countByProductId(
            @Parameter(description = "ID sản phẩm") @PathVariable Integer productId) {
        long count = productCollabService.countByProductId(productId);
        return ResponseEntity.ok(ApiResponse.success("Đếm số lượng collab thành công", count));
    }

    // ==================== QUẢN LÝ THEO COLLAB ====================

    @GetMapping("/collab/{collabId}")
    @Operation(summary = "Lấy danh sách sản phẩm theo collab")
    public ResponseEntity<ApiResponse<List<ProductCollabResponse>>> getByCollabId(
            @Parameter(description = "ID collab") @PathVariable Integer collabId) {
        List<ProductCollabResponse> responses = productCollabService.getByCollabId(collabId);
        return ResponseEntity.ok(ApiResponse.success("Lấy danh sách sản phẩm theo collab thành công", responses));
    }

    @GetMapping("/collab/{collabId}/products")
    @Operation(summary = "Lấy danh sách sản phẩm (response) theo collab")
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getProductsByCollabId(
            @Parameter(description = "ID collab") @PathVariable Integer collabId) {
        List<ProductResponse> responses = productCollabService.getProductsByCollabId(collabId);
        return ResponseEntity.ok(ApiResponse.success("Lấy danh sách sản phẩm theo collab thành công", responses));
    }

    @GetMapping("/collab/{collabId}/product-ids")
    @Operation(summary = "Lấy danh sách ID sản phẩm theo collab")
    public ResponseEntity<ApiResponse<List<Integer>>> getProductIdsByCollabId(
            @Parameter(description = "ID collab") @PathVariable Integer collabId) {
        List<Integer> responses = productCollabService.getProductIdsByCollabId(collabId);
        return ResponseEntity.ok(ApiResponse.success("Lấy danh sách ID sản phẩm thành công", responses));
    }

    @DeleteMapping("/collab/{collabId}")
    @Operation(summary = "Xóa tất cả sản phẩm của collab")
    public ResponseEntity<ApiResponse<Void>> deleteByCollabId(
            @Parameter(description = "ID collab") @PathVariable Integer collabId) {
        productCollabService.deleteByCollabId(collabId);
        return ResponseEntity.ok(ApiResponse.success("Xóa tất cả sản phẩm của collab thành công", null));
    }

    @GetMapping("/collab/{collabId}/count")
    @Operation(summary = "Đếm số lượng sản phẩm của collab")
    public ResponseEntity<ApiResponse<Long>> countByCollabId(
            @Parameter(description = "ID collab") @PathVariable Integer collabId) {
        long count = productCollabService.countByCollabId(collabId);
        return ResponseEntity.ok(ApiResponse.success("Đếm số lượng sản phẩm thành công", count));
    }

    // ==================== BULK OPERATIONS ====================

    @PostMapping("/bulk/product/{productId}")
    @Operation(summary = "Thêm nhiều collab cho sản phẩm")
    public ResponseEntity<ApiResponse<List<ProductCollabResponse>>> addBulkCollabs(
            @Parameter(description = "ID sản phẩm") @PathVariable Integer productId,
            @RequestBody List<Integer> collabIds) {
        List<ProductCollabResponse> responses = productCollabService.saveBulk(productId, collabIds);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Thêm " + responses.size() + " collab thành công", responses));
    }

    @DeleteMapping("/bulk/product/{productId}")
    @Operation(summary = "Xóa nhiều collab khỏi sản phẩm")
    public ResponseEntity<ApiResponse<Void>> deleteBulkCollabs(
            @Parameter(description = "ID sản phẩm") @PathVariable Integer productId,
            @RequestBody List<Integer> collabIds) {
        productCollabService.deleteBulk(productId, collabIds);
        return ResponseEntity.ok(ApiResponse.success("Xóa " + collabIds.size() + " collab thành công", null));
    }

    @PostMapping("/sync")
    @Operation(summary = "Đồng bộ danh sách collab cho sản phẩm")
    public ResponseEntity<ApiResponse<Void>> syncCollabsForProduct(
            @Valid @RequestBody SyncCollabsRequest request) {
        productCollabService.syncCollabsForProduct(request.getProductId(), request.getCollabIds());
        return ResponseEntity.ok(ApiResponse.success("Đồng bộ collab cho sản phẩm thành công", null));
    }

    // ==================== KIỂM TRA ====================

    @GetMapping("/check")
    @Operation(summary = "Kiểm tra sản phẩm đã có collab chưa")
    public ResponseEntity<ApiResponse<Boolean>> checkExists(
            @RequestParam Integer productId,
            @RequestParam Integer collabId,
            @RequestParam(required = false) Integer id) {
        
        boolean exists = id == null
                ? productCollabService.existsByProductIdAndCollabId(productId, collabId)
                : productCollabService.existsByProductIdAndCollabIdAndIdNot(productId, collabId, id);
        
        return ResponseEntity.ok(ApiResponse.success("Kiểm tra thành công", exists));
    }
}