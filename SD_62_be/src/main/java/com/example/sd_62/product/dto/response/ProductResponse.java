package com.example.sd_62.product.dto.response;

import com.example.sd_62.product.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {
    private Integer id;
    private String productCode;
    private String name;
    private Integer productReleaseId;
    private String productReleaseName;
    private Integer brandId;
    private String brandName;
    private Integer genderId;
    private String genderName;
    private Integer materialId;
    private String materialName;
    private Integer formId;
    private String formName;
    private String description;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ProductResponse(Product product) {
        this.id = product.getId();
        this.productCode = product.getProductCode();
        this.name = product.getName();
    }
}