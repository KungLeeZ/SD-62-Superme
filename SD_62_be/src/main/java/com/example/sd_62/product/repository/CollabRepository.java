package com.example.sd_62.product.repository;

import com.example.sd_62.product.entity.Collab;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CollabRepository extends JpaRepository<Collab, Integer> {

    // ===== KIỂM TRA TỒN TẠI =====
    boolean existsByNameIgnoreCase(String name);
    
    boolean existsByNameIgnoreCaseAndIdNot(String name, Integer id);

    // ===== TÌM KIẾM CƠ BẢN =====
    Optional<Collab> findByName(String name);
    
    List<Collab> findByType(String type);
    
    List<Collab> findByYear(Integer year);
    
    List<Collab> findByYearBetween(Integer startYear, Integer endYear);
    
    List<Collab> findByTypeAndYear(String type, Integer year);

    // ===== SẮP XẾP =====
    List<Collab> findAllByOrderByNameAsc();
    
    List<Collab> findAllByOrderByYearDesc();
    
    List<Collab> findByTypeOrderByYearDesc(String type);
}