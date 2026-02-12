package com.example.sd_62.product.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductRequest {

    @NotBlank(message = "Mã sản phẩm không được để trống")
    @Size(max = 255, message = "Mã sản phẩm không được vượt quá 255 ký tự")
    private String productCode;

    @NotBlank(message = "Tên sản phẩm không được để trống")
    @Size(max = 255, message = "Tên sản phẩm không được vượt quá 255 ký tự")
    private String name;

    @NotNull(message = "Thương hiệu không được để trống")
    private Integer brandId;

    private Integer genderId;
    private Integer materialId;
    private Integer formId;
    private Integer productReleaseId;

    @Size(max = 255, message = "Mô tả không được vượt quá 255 ký tự")
    private String description;
}