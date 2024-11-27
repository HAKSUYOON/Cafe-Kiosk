package com.zerobase.cafekiosk.category.repository;

import com.zerobase.cafekiosk.category.entity.Category;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {

  Optional<Category> findByCategoryName(String categoryName);

  List<Category> findByUsingYnTrue();
}
