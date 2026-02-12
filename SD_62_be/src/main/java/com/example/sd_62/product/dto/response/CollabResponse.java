package com.example.sd_62.product.dto.response;

import com.example.sd_62.product.entity.Collab;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CollabResponse {

    private Integer id;
    private String name;
    private String type;
    private Integer year;
    private String displayName;

    public CollabResponse(Collab collab) {
        this.id = collab.getId();
        this.name = collab.getName();
        this.type = collab.getType();
        this.year = collab.getYear();
        this.displayName = collab.getName() + " (" + collab.getYear() + ")";
    }
}