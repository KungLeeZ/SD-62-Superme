package com.example.sd_62.product.dto.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class ProductImageCreateRequest {
    private Integer variantId;
    private Integer sortOrder = 0;
    private MultipartFile imageFile;
}