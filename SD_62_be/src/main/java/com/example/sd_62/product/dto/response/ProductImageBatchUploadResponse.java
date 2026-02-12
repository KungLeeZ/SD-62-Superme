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
public class ProductImageBatchUploadResponse {
    
    private boolean success;
    private String message;
    private int totalFiles;
    private int successfulUploads;
    private int failedUploads;
    
    // Results
    private List<UploadResult> results;
    
    // Summary
    private Long totalFileSize;
    private String averageFileSize;
    private List<String> uploadedFileNames;
    private List<String> failedFileNames;
    
    @Data
    @Builder
    public static class UploadResult {
        private String originalFileName;
        private boolean success;
        private String message;
        private Integer imageId;
        private String imageUrl;
        private String error;
        private Long fileSize;
    }
}