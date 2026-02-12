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
public class ProductImageListResponse {
    
    private boolean success;
    private String message;
    private int totalItems;
    private int currentPage;
    private int totalPages;
    private int pageSize;
    
    // Data
    private List<ProductImageResponse> images;
    
    // Pagination info
    private boolean hasNext;
    private boolean hasPrevious;
    private String nextPageUrl;
    private String previousPageUrl;
    
    // Summary
    private int totalImages;
    private int primaryImages;
    private List<VariantImageSummary> variantSummaries;
    
    @Data
    @Builder
    public static class VariantImageSummary {
        private Integer variantId;
        private String variantName;
        private int imageCount;
        private String primaryImageUrl;
    }
}