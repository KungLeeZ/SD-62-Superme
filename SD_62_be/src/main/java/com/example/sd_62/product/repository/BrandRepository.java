package com.example.sd_62.product.repository;

import com.example.sd_62.product.entity.Brand;
import com.example.sd_62.product.enums.BrandStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Integer> {

    // ===== KIỂM TRA TỒN TẠI =====
    boolean existsByCodeIgnoreCase(String code);

    boolean existsByCodeIgnoreCaseAndIdNot(String code, Integer id);

    boolean existsByNameIgnoreCase(String name);

    boolean existsByNameIgnoreCaseAndIdNot(String name, Integer id);

    // ===== TÌM KIẾM CƠ BẢN =====
    Optional<Brand> findByCode(String code);

    Optional<Brand> findByName(String name);

    List<Brand> findByStatus(BrandStatus status);

    List<Brand> findByStatusOrderByNameAsc(BrandStatus status);

    // ===== SẮP XẾP =====
    List<Brand> findAllByOrderByNameAsc();

    List<Brand> findAllByOrderByCodeAsc();

    List<Brand> findAllByStatusOrderByNameAsc(BrandStatus status);

    // ===== ĐẾM =====
    long countByStatus(BrandStatus status);
}