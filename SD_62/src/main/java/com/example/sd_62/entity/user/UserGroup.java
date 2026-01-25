package com.example.sd_62.entity.user;
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

    @Column(name = "type", columnDefinition = "varchar(255)")
    private String type;

    @Column(name = "description", columnDefinition = "nvarchar(255)")
    private String description;
}

