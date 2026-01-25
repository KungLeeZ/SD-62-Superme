package com.example.sd_62.entity.user;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_oauth")
@Getter
@Setter
public class UserOAuth {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    @Column(name = "provider", columnDefinition = "varchar(255)", nullable = false)
    private String provider;

    @Column(name = "provider_id", columnDefinition = "varchar(255)", nullable = false)
    private String providerId;

    @Column(name = "mail_provider", columnDefinition = "varchar(255)")
    private String mailProvider;

    @Column(name = "create_at")
    private LocalDateTime createAt;
}

