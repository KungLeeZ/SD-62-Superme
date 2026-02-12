package com.example.sd_62.product.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductImageDetailResponse {
    
    private Integer id;
    private Integer variantId;
    private String variantCode;
    private String variantName;
    
    private Integer productId;
    private String productCode;
    private String productName;
    
    // Image URLs
    private String imageUrl;
    private String thumbnailUrl;
    private String mediumImageUrl;
    private String originalImageUrl;
    
    // Metadata
    private String fileName;
    private Long fileSize;
    private String mimeType;
    private String imageFormat;
    private Integer width;
    private Integer height;
    
    // Business fields
    private Integer sortOrder;
    private Boolean isPrimary;
    private String imageType; // "MAIN", "GALLERY", "THUMBNAIL"
    
    // Timestamps
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Links for frontend
    private List<ImageLink> links;
    
    @Data
    @Builder
    public static class ImageLink {
        private String rel;
        private String href;
        private String method;
    }
}