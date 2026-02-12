package com.example.sd_62.product.repository;

import com.example.sd_62.product.entity.Material;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MaterialRepository extends JpaRepository<Material, Integer> {

    // ===== KIỂM TRA TỒN TẠI =====
    boolean existsByNameIgnoreCase(String name);
    
    boolean existsByNameIgnoreCaseAndIdNot(String name, Integer id);

    // ===== TÌM KIẾM CƠ BẢN =====
    Optional<Material> findByName(String name);
    
    List<Material> findByNameContainingIgnoreCase(String keyword);
    
    // ===== SẮP XẾP =====
    List<Material> findAllByOrderByNameAsc();
}