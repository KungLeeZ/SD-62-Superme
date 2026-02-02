package com.example.sd_62.order.entity;
import com.example.sd_62.order.enums.OrderStatus;
import com.example.sd_62.order.enums.OrderType;
import com.example.sd_62.user.entity.User;
import com.example.sd_62.voucher.entity.Voucher;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "`order`")
@Getter
@Setter
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "invoice_code", columnDefinition = "varchar(255)", nullable = false)
    private String invoiceCode;

    @ManyToOne
    @JoinColumn(name = "staff_id", referencedColumnName = "id", nullable = false)
    private User staff;

    @ManyToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    private User customer;

    @Column(name = "cust_name", columnDefinition = "nvarchar(255)")
    private String custName;

    @Column(name = "cust_phone", columnDefinition = "varchar(10)")
    private String custPhone;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "voucher_id", referencedColumnName = "id")
    private Voucher voucher;

    @ManyToOne
    @JoinColumn(name = "payment_id", referencedColumnName = "id", nullable = false)
    private PaymentMethod paymentMethod;

    @Column(name = "vc_discount", precision = 19, scale = 2)
    private BigDecimal vcDiscount;

    @Column(name = "shipping_fee", precision = 19, scale = 2)
    private BigDecimal shippingFee;

    @Column(name = "subtotal", precision = 19, scale = 2)
    private BigDecimal subtotal;

    @Column(name = "total_amount", precision = 19, scale = 2)
    private BigDecimal totalAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_type", columnDefinition = "varchar(30)", nullable = false)
    private OrderType orderType;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", columnDefinition = "varchar(30)", nullable = false)
    private OrderStatus status;
}



