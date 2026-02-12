package com.example.sd_62.product.repository;

import com.example.sd_62.product.entity.Collab;
import com.example.sd_62.product.entity.Product;
import com.example.sd_62.product.entity.ProductCollab;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductCollabRepository extends JpaRepository<ProductCollab, Integer> {

    // ===== KIỂM TRA TỒN TẠI =====
    boolean existsByProductAndCollab(Product product, Collab collab);
    
    boolean existsByProductIdAndCollabId(Integer productId, Integer collabId);
    
    boolean existsByProductIdAndCollabIdAndIdNot(Integer productId, Integer collabId, Integer id);

    // ===== TÌM THEO PRODUCT =====
    List<ProductCollab> findByProductId(Integer productId);
    
    @Query("SELECT pc.collab FROM ProductCollab pc WHERE pc.product.id = :productId")
    List<Collab> findCollabsByProductId(@Param("productId") Integer productId);
    
    @Query("SELECT pc.collab.id FROM ProductCollab pc WHERE pc.product.id = :productId")
    List<Integer> findCollabIdsByProductId(@Param("productId") Integer productId);

    // ===== TÌM THEO COLLAB =====
    List<ProductCollab> findByCollabId(Integer collabId);
    
    @Query("SELECT pc.product FROM ProductCollab pc WHERE pc.collab.id = :collabId")
    List<Product> findProductsByCollabId(@Param("collabId") Integer collabId);
    
    @Query("SELECT pc.product.id FROM ProductCollab pc WHERE pc.collab.id = :collabId")
    List<Integer> findProductIdsByCollabId(@Param("collabId") Integer collabId);

    // ===== TÌM THEO CẢ HAI =====
    Optional<ProductCollab> findByProductIdAndCollabId(Integer productId, Integer collabId);

    // ===== XÓA =====
    @Modifying
    @Transactional
    @Query("DELETE FROM ProductCollab pc WHERE pc.product.id = :productId")
    void deleteByProductId(@Param("productId") Integer productId);
    
    @Modifying
    @Transactional
    @Query("DELETE FROM ProductCollab pc WHERE pc.collab.id = :collabId")
    void deleteByCollabId(@Param("collabId") Integer collabId);
    
    @Modifying
    @Transactional
    void deleteByProductIdAndCollabId(Integer productId, Integer collabId);

    // ===== ĐẾM =====
    long countByProductId(Integer productId);
    
    long countByCollabId(Integer collabId);
}