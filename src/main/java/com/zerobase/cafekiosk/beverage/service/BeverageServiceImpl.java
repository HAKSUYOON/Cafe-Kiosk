package com.zerobase.cafekiosk.beverage.service;

import com.zerobase.cafekiosk.beverage.dto.BeverageDto;
import com.zerobase.cafekiosk.beverage.entity.Beverage;
import com.zerobase.cafekiosk.beverage.model.BeverageInput;
import com.zerobase.cafekiosk.beverage.repository.BeverageRepository;
import com.zerobase.cafekiosk.category.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BeverageServiceImpl implements BeverageService {

  private final CategoryRepository categoryRepository;
  private final BeverageRepository beverageRepository;

  @Override
  public Page<BeverageDto> list(Pageable pageable) {

    Page<Beverage> beverages = beverageRepository.findAll(pageable);

    return beverages.map(BeverageDto::of);
  }

  @Override
  public BeverageDto add(BeverageInput request) {

    if (beverageRepository.existsByBeverageName(request.getBeverageName())) {
      throw new RuntimeException("이미 존재하는 음료명입니다.");
    }

    if (!categoryRepository.existsById(request.getCategoryId())) {
      throw new RuntimeException("해당 카테고리가 존재하지 않습니다.");
    }

    Beverage beverage = new Beverage().buildBeverage(request);
    beverageRepository.save(beverage);

    return BeverageDto.of(beverage);
  }

  @Override
  public void update(Long id, BeverageInput request) {

    Beverage beverage = beverageRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("해당 음료가 존재하지 않습니다."));

    beverage.setBeverage(beverage, request);
    beverageRepository.save(beverage);

  }

  @Override
  public void delete(Long id) {

    if (!beverageRepository.existsById(id)) {
      throw new RuntimeException("존재하지 않는 음료입니다.");
    }

    beverageRepository.deleteById(id);
  }
}
