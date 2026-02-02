package com.example.sd_62.product.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "size")
@Getter
@Setter
public class Size {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "jp", precision = 3, scale = 1)
    private BigDecimal jp;

    @Column(name = "us_men", precision = 3, scale = 1)
    private BigDecimal usMen;

    @Column(name = "us_women", precision = 3, scale = 1)
    private BigDecimal usWomen;

    @Column(name = "eu", precision = 3, scale = 1)
    private BigDecimal eu;

    @Column(name = "foot_length", precision = 3, scale = 1)
    private BigDecimal footLength;
}

