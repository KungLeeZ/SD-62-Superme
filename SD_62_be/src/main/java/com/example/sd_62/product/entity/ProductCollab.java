package com.example.sd_62.product.entity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "product_collab")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductCollab {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "collab_id", referencedColumnName = "id", nullable = false)
    private Collab collab;

    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "id", nullable = false)
    private Product product;
}

