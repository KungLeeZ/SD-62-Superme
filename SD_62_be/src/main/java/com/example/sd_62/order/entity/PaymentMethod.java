package com.example.sd_62.order.entity;
import com.example.sd_62.order.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "payment_method")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentMethod {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name", columnDefinition = "nvarchar(255)", nullable = false)
    private String name;

    @Column(name = "description", columnDefinition = "nvarchar(255)")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", columnDefinition = "varchar(30)", nullable = false)
    private PaymentStatus status;
}


