package com.example.sd_62.product.dto.request;

import com.example.sd_62.product.enums.ProductStatus;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class ProductSearchRequest {
    private String keyword;          // Tìm theo tên hoặc mã sản phẩm
    private Integer brandId;
    private Integer genderId;
    private Integer materialId;
    private Integer formId;
    private Integer productReleaseId;
    private ProductStatus status;    // ACTIVE, INACTIVE
    private LocalDateTime fromDate;  // Tìm từ ngày
    private LocalDateTime toDate;    // Tìm đến ngày
    private String sortBy;           // Sắp xếp theo trường
    private String sortDirection;    // ASC hoặc DESC
}