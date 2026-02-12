package com.example.sd_62.product.entity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "collab")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Collab {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name", columnDefinition = "nvarchar(255)", nullable = false)
    private String name;

    @Column(name = "type", columnDefinition = "varchar(255)")
    private String type;

    @Column(name = "year")
    private Integer year;
}

