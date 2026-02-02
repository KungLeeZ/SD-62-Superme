package com.example.sd_62.user.repository;

import com.example.sd_62.user.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Integer> {

    // Tìm refresh token bằng token string
    Optional<RefreshToken> findByToken(String token);

    // Tìm refresh token còn hiệu lực của user
    @Query("SELECT rt FROM RefreshToken rt WHERE " +
            "rt.user.id = :userId AND " +
            "rt.revoked = false AND " +
            "rt.expiryAt > :now")
    Optional<RefreshToken> findValidTokenByUser(
            @Param("userId") Integer userId,
            @Param("now") LocalDateTime now
    );

    // Tìm tất cả refresh tokens của user
    List<RefreshToken> findByUserId(Integer userId);

    // Tìm refresh tokens đã hết hạn
    List<RefreshToken> findByExpiryAtBefore(LocalDateTime now);

    // Tìm refresh tokens đã bị thu hồi
    List<RefreshToken> findByRevokedTrue();

    // Xóa refresh tokens đã hết hạn
    @Modifying
    @Query("DELETE FROM RefreshToken rt WHERE rt.expiryAt < :now")
    void deleteExpiredTokens(@Param("now") LocalDateTime now);

    // Thu hồi tất cả refresh tokens của user
    @Modifying
    @Query("UPDATE RefreshToken rt SET rt.revoked = true WHERE rt.user.id = :userId")
    void revokeAllUserTokens(@Param("userId") Integer userId);

    // Kiểm tra token có tồn tại và còn hiệu lực không
    @Query("SELECT CASE WHEN COUNT(rt) > 0 THEN true ELSE false END " +
            "FROM RefreshToken rt WHERE " +
            "rt.token = :token AND " +
            "rt.revoked = false AND " +
            "rt.expiryAt > :now")
    boolean isValidToken(@Param("token") String token, @Param("now") LocalDateTime now);
}