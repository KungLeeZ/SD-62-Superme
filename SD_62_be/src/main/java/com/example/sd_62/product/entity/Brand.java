package com.example.sd_62.product.entity;
import com.example.sd_62.product.enums.BrandStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "brand")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Brand {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "code", columnDefinition = "varchar(255)", nullable = false)
    private String code;

    @Column(name = "name", columnDefinition = "nvarchar(255)", nullable = false)
    private String name;

    @Column(name = "description", columnDefinition = "nvarchar(255)")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", columnDefinition = "varchar(30)", nullable = false)
    private BrandStatus status;
}


