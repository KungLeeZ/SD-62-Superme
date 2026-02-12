package com.example.sd_62.product.dto.response;

import com.example.sd_62.product.entity.ProductImage;
import lombok.Getter;
import lombok.Setter;

import java.time.format.DateTimeFormatter;

@Getter
@Setter
public class ProductImageResponse {

    private Integer id;
    private Integer variantId;
    private String productName;
    private String skuVariant;
    private String imageUrl;
    private String fullImageUrl;
    private Integer sortOrder;
    private String createdAt;
    private String createdAtFormatted;

    public ProductImageResponse(ProductImage image) {
        this.id = image.getId();
        this.variantId = image.getProductVariant().getId();
        
        if (image.getProductVariant().getProduct() != null) {
            this.productName = image.getProductVariant().getProduct().getName();
        }
        this.skuVariant = image.getProductVariant().getSkuVariant();
        
        this.imageUrl = image.getImageUrl();
        this.fullImageUrl = image.getImageUrl(); // Có thể thêm base URL ở frontend
        this.sortOrder = image.getSortOrder();
        this.createdAt = image.getCreatedAt() != null ? image.getCreatedAt().toString() : null;
        
        if (image.getCreatedAt() != null) {
            this.createdAtFormatted = image.getCreatedAt()
                    .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
        }
    }
}