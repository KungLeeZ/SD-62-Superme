package com.example.sd_62.product.entity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "gender")
@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Gender {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name", columnDefinition = "nvarchar(50)", nullable = false)
    private String name;

    @Column(name = "description", columnDefinition = "nvarchar(255)")
    private String description;
}

