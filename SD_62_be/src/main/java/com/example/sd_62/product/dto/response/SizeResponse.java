package com.example.sd_62.product.dto.response;

import com.example.sd_62.product.entity.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class SizeResponse {

    private Integer id;
    private BigDecimal jp;
    private BigDecimal usMen;
    private BigDecimal usWomen;
    private BigDecimal eu;
    private BigDecimal footLength;
    
    // Hiển thị dễ đọc
    private String displayName;

    public SizeResponse(Size size) {
        this.id = size.getId();
        this.jp = size.getJp();
        this.usMen = size.getUsMen();
        this.usWomen = size.getUsWomen();
        this.eu = size.getEu();
        this.footLength = size.getFootLength();
        
        // Format: JP 25.0 (EU 40, US 7.0)
        this.displayName = String.format("JP %s (EU %s, US Men %s)", 
            size.getJp() != null ? size.getJp().toString() : "N/A",
            size.getEu() != null ? size.getEu().toString() : "N/A",
            size.getUsMen() != null ? size.getUsMen().toString() : "N/A");
    }
}