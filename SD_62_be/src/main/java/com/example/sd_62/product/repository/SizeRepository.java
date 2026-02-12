package com.example.sd_62.product.repository;

import com.example.sd_62.product.entity.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface SizeRepository extends JpaRepository<Size, Integer> {

    // Lấy tất cả sắp xếp theo JP (size Nhật)
    List<Size> findAllByOrderByJpAsc();
    
    // Tìm size theo JP
    Optional<Size> findByJp(BigDecimal jp);
    
    // Tìm size theo EU
    Optional<Size> findByEu(BigDecimal eu);
    
    // Tìm size theo US Men
    Optional<Size> findByUsMen(BigDecimal usMen);
    
    // Tìm size theo US Women
    Optional<Size> findByUsWomen(BigDecimal usWomen);
    
    // Tìm size theo foot length
    Optional<Size> findByFootLength(BigDecimal footLength);
    
    // Tìm kiếm size trong khoảng
    List<Size> findByJpBetween(BigDecimal from, BigDecimal to);
}