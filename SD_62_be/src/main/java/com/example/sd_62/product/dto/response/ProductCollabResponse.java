package com.example.sd_62.product.dto.response;

import com.example.sd_62.product.entity.ProductCollab;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductCollabResponse {

    private Integer id;
    
    // Product info
    private Integer productId;
    private String productName;
    private String productCode;
    
    // Collab info
    private Integer collabId;
    private String collabName;
    private String collabType;
    private Integer collabYear;
    
    private String displayName;

    public ProductCollabResponse(ProductCollab productCollab) {
        this.id = productCollab.getId();
        
        if (productCollab.getProduct() != null) {
            this.productId = productCollab.getProduct().getId();
            this.productName = productCollab.getProduct().getName();
            this.productCode = productCollab.getProduct().getProductCode();
        }
        
        if (productCollab.getCollab() != null) {
            this.collabId = productCollab.getCollab().getId();
            this.collabName = productCollab.getCollab().getName();
            this.collabType = productCollab.getCollab().getType();
            this.collabYear = productCollab.getCollab().getYear();
        }
        
        this.displayName = this.collabName + " x " + this.productName;
    }
}