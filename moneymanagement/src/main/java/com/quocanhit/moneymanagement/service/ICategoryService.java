package com.quocanhit.moneymanagement.service;

import com.quocanhit.moneymanagement.dto.category.CategoryDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface ICategoryService {
    ResponseEntity<?> createCategory(CategoryDTO categoryDTO);

    ResponseEntity<?> getListCategoryByProfileId();
}
