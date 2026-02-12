package com.example.sd_62.user.entity;
import com.example.sd_62.user.enums.UserGroupType;
import com.example.sd_62.user.enums.UserStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "`user`")
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name", columnDefinition = "nvarchar(255)", nullable = false)
    private String name;

    @Column(name = "email", columnDefinition = "varchar(255)")
    private String email;

    @Enumerated(EnumType.STRING)
    @ManyToOne
    @JoinColumn(name = "group_id", referencedColumnName = "id", nullable = false)
    private UserGroup group;

    @Column(name = "phone", columnDefinition = "varchar(255)")
    private String phone;

    @Column(name = "password", columnDefinition = "varchar(255)")
    private String password;

    @Column(name = "create_at")
    private LocalDateTime createAt;

    @Column(name = "update_at")
    private LocalDateTime updateAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", columnDefinition = "varchar(30)", nullable = false)
    private UserStatus status;

    @Column(name = "avatar_url", columnDefinition = "varchar(500)")
    private String avatarUrl;

    @Column(name = "email_verified")
    private Boolean emailVerified = false;

    public static User fromOAuth2(String email, String name, String avatarUrl) {
        User user = new User();
        user.setEmail(email);
        user.setName(name);
        user.setAvatarUrl(avatarUrl);
        user.setEmailVerified(true);
        user.setStatus(UserStatus.ACTIVE);
        user.setCreateAt(LocalDateTime.now());
        user.setUpdateAt(LocalDateTime.now());
        return user;
    }
}



