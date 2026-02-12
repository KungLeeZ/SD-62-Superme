package com.example.sd_62.product.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ColorRequest {

    @NotBlank(message = "Tên màu không được để trống")
    @Size(max = 50, message = "Tên màu không được vượt quá 50 ký tự")
    private String name;

    @Pattern(regexp = "^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$", message = "Mã hex không đúng định dạng (ví dụ: #FF0000)")
    @Size(max = 7, message = "Mã hex không được vượt quá 7 ký tự")
    private String hex;

    @Size(max = 255, message = "Mô tả không được vượt quá 255 ký tự")
    private String description;
}