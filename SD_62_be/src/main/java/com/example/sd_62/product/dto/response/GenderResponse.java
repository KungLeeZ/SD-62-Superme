package com.example.sd_62.product.dto.response;

import com.example.sd_62.product.entity.Gender;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GenderResponse {

    private Integer id;
    private String name;
    private String description;

    public GenderResponse(Gender gender) {
        this.id = gender.getId();
        this.name = gender.getName();
        this.description = gender.getDescription();
    }
}