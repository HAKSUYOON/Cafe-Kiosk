package com.zerobase.cafekiosk.admin.category.controller;

import com.zerobase.cafekiosk.admin.category.dto.CategoryDto;
import com.zerobase.cafekiosk.admin.category.model.CategoryInput;
import com.zerobase.cafekiosk.admin.category.service.CategoryService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@RequiredArgsConstructor
@Controller
public class CategoryController {

  private final CategoryService categoryService;

  @GetMapping("/admin/category/list.do")
  public String list(Model model) {

    List<CategoryDto> categoryDtoList = categoryService.list();
    model.addAttribute("list", categoryDtoList);

    return "admin/category/list";
  }

  @PostMapping("/admin/category/add.do")
  public String add(Model model, CategoryInput parameter) {

    boolean result = categoryService.add(parameter.getCategoryName());
    model.addAttribute("result", result);

    return "redirect:/admin/category/list.do";
  }

  @PostMapping("/admin/category/delete.do")
  public String delete(Model model, CategoryInput parameter) {

    boolean result = categoryService.delete(parameter.getId());

    return "redirect:/admin/category/list.do";
  }

  @PostMapping("/admin/category/update.do")
  public String update(Model model, CategoryInput parameter) {

    boolean result = categoryService.update(parameter);

    return "redirect:/admin/category/list.do";
  }
}
