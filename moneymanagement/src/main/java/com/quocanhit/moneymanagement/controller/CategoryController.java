package com.quocanhit.moneymanagement.controller;

import com.quocanhit.moneymanagement.constant.EndpointConst;
import com.quocanhit.moneymanagement.dto.category.CategoryDTO;
import com.quocanhit.moneymanagement.service.ICategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CategoryController {

    private final ICategoryService categoryService;

    @PostMapping(EndpointConst.CATEGORY)
    public ResponseEntity<?> createCategory(@RequestBody CategoryDTO categoryDTO) {
        return categoryService.createCategory(categoryDTO);
    }

    @GetMapping(EndpointConst.CATEGORIES)
    public ResponseEntity<?> getListCategoryByProfileId() {
        return categoryService.getListCategoryByProfileId();
    }
}

