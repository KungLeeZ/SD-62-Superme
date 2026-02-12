package com.example.sd_62.product.dto.request;

import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter
public class ProductVariantSearchRequest {
    private Integer productId;
    private Integer sizeId;
    private Integer colorId;
    private String keyword;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private String status;
    private Integer minQuantity;
    private Integer maxQuantity;
    private String sortBy;
    private String sortDirection;
}