package com.zerobase.cafekiosk.beverage.service;

import com.zerobase.cafekiosk.beverage.dto.BeverageDto;
import com.zerobase.cafekiosk.beverage.entity.Beverage;
import com.zerobase.cafekiosk.beverage.model.BeverageInput;
import com.zerobase.cafekiosk.beverage.repository.BeverageRepository;
import com.zerobase.cafekiosk.category.repository.CategoryRepository;
import com.zerobase.cafekiosk.exception.Impl.AlreadyExistsBeverageException;
import com.zerobase.cafekiosk.exception.Impl.NotFoundBeverageException;
import com.zerobase.cafekiosk.exception.Impl.NotFoundCategoryException;
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
      throw new AlreadyExistsBeverageException();
    }

    if (!categoryRepository.existsById(request.getCategoryId())) {
      throw new NotFoundCategoryException();
    }

    Beverage beverage = new Beverage().buildBeverage(request);
    beverageRepository.save(beverage);

    return BeverageDto.of(beverage);
  }

  @Override
  public void update(Long id, BeverageInput request) {

    Beverage beverage = beverageRepository.findById(id)
        .orElseThrow(NotFoundBeverageException::new);

    beverage.setBeverage(beverage, request);
    beverageRepository.save(beverage);

  }

  @Override
  public void delete(Long id) {

    if (!beverageRepository.existsById(id)) {
      throw new NotFoundBeverageException();
    }

    beverageRepository.deleteById(id);
  }

  @Override
  public Page<BeverageDto> frontBeverages(Long categoryId, Pageable pageable) {

    return beverageRepository.findAllByCategoryId(categoryId, pageable).map(BeverageDto::of);
  }

  @Override
  public BeverageDto beverageDetail(Long beverageId) {

    Beverage beverage = beverageRepository.findById(beverageId)
        .orElseThrow(NotFoundBeverageException::new);

    return BeverageDto.of(beverage);
  }
}
