package com.example.sd_62.product.dto.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
public class ProductImageBatchUploadRequest {
    private Integer variantId;
    private List<MultipartFile> imageFiles;
    private Integer startSortOrder = 0;
}