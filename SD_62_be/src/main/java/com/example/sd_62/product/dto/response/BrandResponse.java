package com.example.sd_62.product.dto.response;

import com.example.sd_62.product.entity.Brand;
import com.example.sd_62.product.enums.BrandStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BrandResponse {

    private Integer id;
    private String code;
    private String name;
    private String description;
    private BrandStatus status;
    private String statusName;

    public BrandResponse(Brand brand) {
        this.id = brand.getId();
        this.code = brand.getCode();
        this.name = brand.getName();
        this.description = brand.getDescription();
        this.status = brand.getStatus();
        this.statusName = brand.getStatus() != null ? brand.getStatus().name() : null;
    }
}