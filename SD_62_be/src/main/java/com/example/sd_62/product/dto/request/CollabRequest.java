package com.example.sd_62.product.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CollabRequest {

    @NotBlank(message = "Tên collab không được để trống")
    @Size(max = 255, message = "Tên collab không được vượt quá 255 ký tự")
    private String name;

    @Size(max = 255, message = "Type không được vượt quá 255 ký tự")
    private String type;

    @NotNull(message = "Năm không được để trống")
    @Min(value = 2000, message = "Năm phải lớn hơn hoặc bằng 2000")
    private Integer year;
}