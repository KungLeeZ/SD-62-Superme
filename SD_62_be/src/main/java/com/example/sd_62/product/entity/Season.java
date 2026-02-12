package com.example.sd_62.product.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "season")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Season {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "season_code", columnDefinition = "varchar(255)", nullable = false)
    private String seasonCode;

    @Column(name = "name", columnDefinition = "nvarchar(255)", nullable = false)
    private String name;

    @Column(name = "year")
    private Integer year;

    @Column(name = "description", columnDefinition = "nvarchar(255)")
    private String description;
}


