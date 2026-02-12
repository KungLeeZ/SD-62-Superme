package com.example.sd_62.product.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductImageRequest {

    private Integer variantId;
    private Integer sortOrder = 0;
    private String imageUrl; // URL từ upload service trả về
}