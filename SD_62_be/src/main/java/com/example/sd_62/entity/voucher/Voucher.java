package com.example.sd_62.entity.voucher;
import com.example.sd_62.enums.VoucherStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "voucher")
@Getter
@Setter
public class Voucher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "code", columnDefinition = "varchar(255)", nullable = false)
    private String code;

    @Column(name = "type", columnDefinition = "varchar(255)", nullable = false)
    private String type;

    @Column(name = "value", precision = 19, scale = 2)
    private BigDecimal value;

    @Column(name = "max_discount", precision = 19, scale = 2)
    private BigDecimal maxDiscount;

    @Column(name = "min_order", precision = 19, scale = 2)
    private BigDecimal minOrder;

    @Column(name = "usage_limit")
    private Integer usageLimit;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "start_at")
    private LocalDateTime startAt;

    @Column(name = "end_at")
    private LocalDateTime endAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", columnDefinition = "varchar(30)", nullable = false)
    private VoucherStatus status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}


