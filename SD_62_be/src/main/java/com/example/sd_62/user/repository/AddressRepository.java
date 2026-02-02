package com.example.sd_62.user.repository;

import com.example.sd_62.user.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, Integer> {

    // Tìm tất cả addresses của user
    List<Address> findByUserId(Integer userId);

    // Tìm address mặc định của user
    Optional<Address> findByUserIdAndIsDefaultTrue(Integer userId);

    // Tìm addresses theo type
    List<Address> findByUserIdAndType(Integer userId, String type);

    // Kiểm tra user có address mặc định chưa
    boolean existsByUserIdAndIsDefaultTrue(Integer userId);

    // Đếm số addresses của user
    long countByUserId(Integer userId);

    // Hủy tất cả addresses mặc định của user
    @Modifying
    @Query("UPDATE Address a SET a.isDefault = false WHERE a.user.id = :userId")
    void unsetAllDefaultAddresses(@Param("userId") Integer userId);

    // Xóa tất cả addresses của user
    void deleteByUserId(Integer userId);
}