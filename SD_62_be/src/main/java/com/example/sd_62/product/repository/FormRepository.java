package com.example.sd_62.product.repository;

import com.example.sd_62.product.entity.Form;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FormRepository extends JpaRepository<Form, Integer> {

    // ===== KIỂM TRA TỒN TẠI =====
    boolean existsByNameIgnoreCase(String name);
    
    boolean existsByNameIgnoreCaseAndIdNot(String name, Integer id);

    // ===== TÌM KIẾM CƠ BẢN =====
    Optional<Form> findByName(String name);
    
    List<Form> findByNameContainingIgnoreCase(String keyword);
    
    // ===== SẮP XẾP =====
    List<Form> findAllByOrderByNameAsc();
}