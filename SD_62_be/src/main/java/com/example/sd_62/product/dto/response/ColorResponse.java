package com.example.sd_62.product.dto.response;

import com.example.sd_62.product.entity.Color;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ColorResponse {

    private Integer id;
    private String name;
    private String hex;
    private String description;
    private String displayColor;

    public ColorResponse(Color color) {
        this.id = color.getId();
        this.name = color.getName();
        this.hex = color.getHex();
        this.description = color.getDescription();
        this.displayColor = color.getName() + " (" + (color.getHex() != null ? color.getHex() : "N/A") + ")";
    }
}