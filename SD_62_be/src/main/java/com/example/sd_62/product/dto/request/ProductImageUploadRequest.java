package com.example.sd_62.product.dto.request;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class ProductImageUploadRequest {

    private Integer variantId;
    private Integer sortOrder = 0;

    // Cho upload mới
    private MultipartFile imageFile;

    // Cho update URL
    private String imageUrl;

    // Cho update chỉ metadata
    private Boolean keepExistingFile = true;
}