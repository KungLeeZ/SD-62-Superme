package com.example.sd_62.product.service;

import com.example.sd_62.product.dto.response.ProductImageResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductImageService {

    // ===== UPLOAD =====
    ProductImageResponse uploadImage(Integer variantId, MultipartFile file, Integer sortOrder);
    
    List<ProductImageResponse> uploadMultipleImages(Integer variantId, List<MultipartFile> files);
    
    // ===== GET =====
    ProductImageResponse getImageById(Integer imageId);
    
    List<ProductImageResponse> getImagesByVariantId(Integer variantId);
    
    // ===== UPDATE =====
    ProductImageResponse updateSortOrder(Integer imageId, Integer sortOrder);
    
    ProductImageResponse updateImageUrl(Integer imageId, String newImageUrl);
    
    // ===== DELETE =====
    void deleteImage(Integer imageId);
    
    void deleteAllImagesByVariantId(Integer variantId);
    
    void deleteImagesByIds(List<Integer> imageIds);
    
    // ===== VALIDATE & UTILITY =====
    boolean isImageBelongToVariant(Integer imageId, Integer variantId);
    
    long countImagesByVariantId(Integer variantId);
    
    String getImagePath(String imageUrl);
}