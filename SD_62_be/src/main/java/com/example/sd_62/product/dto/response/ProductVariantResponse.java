package com.example.sd_62.product.dto.response;

import com.example.sd_62.product.entity.ProductVariant;
import com.example.sd_62.product.enums.ProductStatus;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
public class ProductVariantResponse {

    private Integer id;
    private String skuVariant;
    private String productName;
    private Integer productId;
    private String sizeValue;
    private Integer sizeId;
    private String colorName;
    private Integer colorId;
    private String colorHex;
    private Integer quantity;
    private BigDecimal price;
    private String priceFormatted;
    private String description;
    private ProductStatus status;
    private String statusName;
    private String createdAt;
    private String updatedAt;

    public ProductVariantResponse(ProductVariant variant) {
        this.id = variant.getId();
        this.skuVariant = variant.getSkuVariant();

        if (variant.getProduct() != null) {
            this.productId = variant.getProduct().getId();
            this.productName = variant.getProduct().getName();
        }

        if (variant.getSize() != null) {
            this.sizeId = variant.getSize().getId();
            this.sizeValue = String.valueOf(variant.getSize().getJp());
        }

        if (variant.getColor() != null) {
            this.colorId = variant.getColor().getId();
            this.colorName = variant.getColor().getName();
            this.colorHex = variant.getColor().getHex();
        }

        this.quantity = variant.getQuantity();
        this.price = variant.getPrice();
        this.priceFormatted = String.format("%,d VNĐ", variant.getPrice().longValue());
        this.description = variant.getDescription();
        this.status = variant.getStatus();
        this.statusName = variant.getStatus() != null ? variant.getStatus().name() : null;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        this.createdAt = variant.getCreatedAt() != null ? variant.getCreatedAt().format(formatter) : null;
        this.updatedAt = variant.getUpdatedAt() != null ? variant.getUpdatedAt().format(formatter) : null;
    }
}