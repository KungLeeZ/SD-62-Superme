package com.example.sd_62.product.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GenderRequest {

    @NotBlank(message = "Tên giới tính không được để trống")
    @Size(max = 50, message = "Tên giới tính không được vượt quá 50 ký tự")
    private String name;

    @Size(max = 255, message = "Mô tả không được vượt quá 255 ký tự")
    private String description;
}