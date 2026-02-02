package com.example.sd_62.user.entity;
import com.example.sd_62.user.enums.AddressType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "address")
@Getter
@Setter
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    @Column(name = "cust_name", columnDefinition = "nvarchar(255)")
    private String custName;

    @Column(name = "cust_phone", columnDefinition = "varchar(255)")
    private String custPhone;

    @Column(name = "province", columnDefinition = "nvarchar(255)")
    private String province;

    @Column(name = "district", columnDefinition = "nvarchar(255)")
    private String district;

    @Column(name = "ward", columnDefinition = "nvarchar(255)")
    private String ward;

    @Column(name = "street", columnDefinition = "nvarchar(255)")
    private String street;

    @Column(name = "is_default")
    private Boolean isDefault;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", columnDefinition = "varchar(30)")
    private AddressType type;
}


