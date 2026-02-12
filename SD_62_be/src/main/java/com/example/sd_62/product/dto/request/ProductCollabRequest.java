package com.example.sd_62.product.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductCollabRequest {

    @NotNull(message = "ID sản phẩm không được để trống")
    private Integer productId;

    @NotNull(message = "ID collab không được để trống")
    private Integer collabId;
}