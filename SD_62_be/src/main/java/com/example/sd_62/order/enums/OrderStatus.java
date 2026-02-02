package com.example.sd_62.order.enums;

public enum OrderStatus {

    CREATED,        // tạo đơn (chưa xác nhận)
    CONFIRMED,      // đã xác nhận
    PAID,           // đã thanh toán
    PROCESSING,     // đang chuẩn bị hàng
    SHIPPED,        // đã giao cho vận chuyển
    COMPLETED,      // giao thành công
    CANCELLED       // huỷ đơn
}

