package com.example.sd_62.user.entity;
import com.example.sd_62.user.enums.UserGroupType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "user_group")
@Getter
@Setter
public class UserGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name", columnDefinition = "nvarchar(255)", nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", columnDefinition = "varchar(50)")
    private UserGroupType type;

    @Column(name = "description", columnDefinition = "nvarchar(255)")
    private String description;
}

