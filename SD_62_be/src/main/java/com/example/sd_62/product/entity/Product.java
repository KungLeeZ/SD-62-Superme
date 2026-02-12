package com.example.sd_62.product.entity;

import com.example.sd_62.product.enums.ProductStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "product")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "product_code", columnDefinition = "varchar(255)", nullable = false)
    private String productCode;

    @Column(name = "name", columnDefinition = "nvarchar(255)", nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "product_release_id", referencedColumnName = "id")
    private ProductRelease productRelease;

    @ManyToOne
    @JoinColumn(name = "brand_id", referencedColumnName = "id", nullable = false)
    private Brand brand;

    @ManyToOne
    @JoinColumn(name = "gender_id", referencedColumnName = "id")
    private Gender gender;

    @ManyToOne
    @JoinColumn(name = "material_id", referencedColumnName = "id")
    private Material material;

    @ManyToOne
    @JoinColumn(name = "form_id", referencedColumnName = "id")
    private Form form;

    @Column(name = "description", columnDefinition = "nvarchar(255)")
    private String description;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", columnDefinition = "varchar(30)", nullable = false)
    private ProductStatus status;
}


