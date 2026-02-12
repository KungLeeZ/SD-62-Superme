package com.example.sd_62.product.dto.response;

import com.example.sd_62.product.entity.Material;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MaterialResponse {

    private Integer id;
    private String name;
    private String description;

    public MaterialResponse(Material material) {
        this.id = material.getId();
        this.name = material.getName();
        this.description = material.getDescription();
    }
}