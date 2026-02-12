package com.example.sd_62.product.repository;

import com.example.sd_62.product.entity.Season;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SeasonRepository extends JpaRepository<Season, Integer> {

    // ===== KIỂM TRA TỒN TẠI =====
    boolean existsBySeasonCodeIgnoreCase(String seasonCode);
    
    boolean existsBySeasonCodeIgnoreCaseAndIdNot(String seasonCode, Integer id);
    
    boolean existsByNameIgnoreCase(String name);
    
    boolean existsByNameIgnoreCaseAndIdNot(String name, Integer id);

    // ===== TÌM KIẾM CƠ BẢN =====
    Optional<Season> findBySeasonCode(String seasonCode);
    
    Optional<Season> findByName(String name);
    
    List<Season> findByYear(Integer year);
    
    List<Season> findByYearBetween(Integer startYear, Integer endYear);
    
    List<Season> findBySeasonCodeContainingIgnoreCaseOrNameContainingIgnoreCase(String code, String name);
    
    Page<Season> findBySeasonCodeContainingIgnoreCaseOrNameContainingIgnoreCase(
            String code, String name, Pageable pageable);

    // ===== SẮP XẾP =====
    List<Season> findAllByOrderByYearDescNameAsc();
    
    Page<Season> findAllByOrderByYearDescNameAsc(Pageable pageable);
    
    List<Season> findAllByOrderByYearAsc();
    
    // ===== THỐNG KÊ =====
    long countByYear(Integer year);
}