package com.example.sd_62.entity.campaign;
import com.example.sd_62.entity.product.Product;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "sale_campaign_product")
@Getter
@Setter
public class SaleCampaignProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "sale_campaign_id", referencedColumnName = "id", nullable = false)
    private SaleCampaign saleCampaign;

    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "id", nullable = false)
    private Product product;

    @Column(name = "discount_price", precision = 19, scale = 2)
    private BigDecimal discountPrice;
}

