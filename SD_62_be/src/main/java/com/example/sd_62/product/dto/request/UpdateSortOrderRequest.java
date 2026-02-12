package com.example.sd_62.product.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateSortOrderRequest {

    @NotNull(message = "Thứ tự không được để trống")
    @Min(value = 0, message = "Thứ tự phải lớn hơn hoặc bằng 0")
    private Integer sortOrder;
}