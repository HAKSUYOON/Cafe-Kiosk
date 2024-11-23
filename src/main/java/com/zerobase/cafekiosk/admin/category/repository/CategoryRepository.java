package com.zerobase.cafekiosk.admin.category.repository;

import com.zerobase.cafekiosk.admin.category.entity.Category;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {

  Optional<Category> findByCategoryName(String categoryName);
}
