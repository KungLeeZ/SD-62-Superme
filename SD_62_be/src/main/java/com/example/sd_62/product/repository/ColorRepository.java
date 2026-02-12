package com.example.sd_62.product.repository;

import com.example.sd_62.product.entity.Color;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ColorRepository extends JpaRepository<Color, Integer> {

    // ===== KIỂM TRA TỒN TẠI =====
    boolean existsByNameIgnoreCase(String name);
    
    boolean existsByNameIgnoreCaseAndIdNot(String name, Integer id);
    
    boolean existsByHexIgnoreCase(String hex);
    
    boolean existsByHexIgnoreCaseAndIdNot(String hex, Integer id);

    // ===== TÌM KIẾM CƠ BẢN =====
    Optional<Color> findByName(String name);
    
    Optional<Color> findByHex(String hex);
    
    List<Color> findByNameContainingIgnoreCase(String keyword);
    
    // ===== SẮP XẾP =====
    List<Color> findAllByOrderByNameAsc();
    
    List<Color> findAllByOrderByHexAsc();
}