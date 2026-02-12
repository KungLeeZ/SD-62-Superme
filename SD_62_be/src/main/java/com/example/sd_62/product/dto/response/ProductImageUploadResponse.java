package com.example.sd_62.product.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductImageUploadResponse {
    
    private boolean success;
    private String message;
    private String code; // "SUCCESS", "ERROR", etc.
    
    // Image data
    private Integer imageId;
    private Integer variantId;
    private String imageUrl;
    
    // Multiple size URLs
    private String thumbnailUrl;
    private String mediumUrl;
    private String largeUrl;
    private String originalUrl;
    
    // File info
    private String originalFileName;
    private String storedFileName;
    private Long fileSize;
    private String mimeType;
    private String fileExtension;
    
    // Sort order
    private Integer sortOrder;
    
    // Timestamp
    private String uploadedAt;
    
    // Error handling
    private String errorCode;
    private String errorDetails;
    
    // Validation info
    private Boolean isValidated;
    private List<String> validationMessages;
}