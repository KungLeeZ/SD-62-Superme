package com.example.sd_62.product.enums;

public enum ProductStatus {

    ACTIVE,         // đang bán
    INACTIVE,       // ẩn khỏi shop (admin tắt)
    OUT_OF_STOCK,   // hết tồn kho (variant = 0)
    DISCONTINUED   // ngừng kinh doanh vĩnh viễn
}
