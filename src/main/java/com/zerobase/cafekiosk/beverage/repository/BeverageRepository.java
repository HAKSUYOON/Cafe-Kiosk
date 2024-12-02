package com.zerobase.cafekiosk.beverage.repository;

import com.zerobase.cafekiosk.beverage.entity.Beverage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BeverageRepository extends JpaRepository<Beverage, Long> {

  boolean existsByBeverageName(String beverageName);

  Page<Beverage> findAllByCategoryId(Long categoryId, Pageable pageable);
}
