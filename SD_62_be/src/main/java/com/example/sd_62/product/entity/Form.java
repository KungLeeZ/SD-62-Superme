package com.example.sd_62.product.entity;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "form")
@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Form {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name", columnDefinition = "nvarchar(100)", nullable = false)
    private String name;

    @Column(name = "description", columnDefinition = "nvarchar(255)")
    private String description;
}

