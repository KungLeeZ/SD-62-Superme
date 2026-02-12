package com.example.sd_62.product.dto.response;

import com.example.sd_62.product.entity.Season;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SeasonResponse {

    private Integer id;
    private String seasonCode;
    private String name;
    private Integer year;
    private String description;
    private String displayName;

    public SeasonResponse(Season season) {
        this.id = season.getId();
        this.seasonCode = season.getSeasonCode();
        this.name = season.getName();
        this.year = season.getYear();
        this.description = season.getDescription();
        this.displayName = season.getName() + " (" + season.getYear() + ")";
    }
}