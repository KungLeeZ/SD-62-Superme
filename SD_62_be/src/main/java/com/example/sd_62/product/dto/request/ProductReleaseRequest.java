package com.example.sd_62.product.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ProductReleaseRequest {

    @NotBlank(message = "Mã đợt phát hành không được để trống")
    @Size(max = 255, message = "Mã đợt phát hành không được vượt quá 255 ký tự")
    private String code;

    @NotBlank(message = "Tên đợt phát hành không được để trống")
    @Size(max = 255, message = "Tên đợt phát hành không được vượt quá 255 ký tự")
    private String name;

    @NotNull(message = "Mùa không được để trống")
    private Integer seasonId;

    private LocalDateTime startDate;
    
    private LocalDateTime endDate;
    
    // Không bao gồm status, createdAt, updatedAt - tự động xử lý trong service
}