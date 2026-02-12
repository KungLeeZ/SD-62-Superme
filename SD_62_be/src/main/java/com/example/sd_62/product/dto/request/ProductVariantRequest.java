package com.example.sd_62.product.dto.request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ProductVariantRequest {

    @NotBlank(message = "Mã SKU variant không được để trống")
    @Size(max = 255, message = "Mã SKU variant không được vượt quá 255 ký tự")
    private String skuVariant;

    @NotNull(message = "ID sản phẩm không được để trống")
    private Integer productId;

    @NotNull(message = "ID size không được để trống")
    private Integer sizeId;

    @NotNull(message = "ID màu sắc không được để trống")
    private Integer colorId;

    @Min(value = 0, message = "Số lượng không thể âm")
    private Integer quantity = 0;

    @DecimalMin(value = "0.0", inclusive = false, message = "Giá phải lớn hơn 0")
    @DecimalMax(value = "999999999.99", message = "Giá quá lớn")
    @NotNull(message = "Giá không được để trống")
    private BigDecimal price;

    @Size(max = 255, message = "Mô tả không được vượt quá 255 ký tự")
    private String description;
}