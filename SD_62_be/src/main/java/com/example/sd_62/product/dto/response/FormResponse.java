package com.example.sd_62.product.dto.response;

import com.example.sd_62.product.entity.Form;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FormResponse {

    private Integer id;
    private String name;
    private String description;

    public FormResponse(Form form) {
        this.id = form.getId();
        this.name = form.getName();
        this.description = form.getDescription();
    }
}