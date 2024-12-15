package com.zerobase.cafekiosk.admin.repository;

import com.zerobase.cafekiosk.admin.entity.StampHistory;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StampHistoryRepository extends JpaRepository<StampHistory, Long> {

  Optional<List<StampHistory>> findByUsername(String username);
}
