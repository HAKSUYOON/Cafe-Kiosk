package com.zerobase.cafekiosk.beverage.service;

import com.zerobase.cafekiosk.beverage.dto.BeverageDto;
import com.zerobase.cafekiosk.beverage.model.BeverageInput;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BeverageService {

  Page<BeverageDto> list(Pageable pageable);

  BeverageDto add(BeverageInput request);

  void update(Long id, BeverageInput request);

  void delete(Long id);
}
