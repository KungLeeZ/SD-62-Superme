package com.example.sd_62.product.repository;

import com.example.sd_62.product.entity.Gender;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GenderRepository extends JpaRepository<Gender, Integer> {

    List<Gender> findAllByOrderByNameAsc();
}