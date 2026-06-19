package com.quocanhit.moneymanagement.service.implement;

import com.quocanhit.moneymanagement.dto.category.CategoryDTO;
import com.quocanhit.moneymanagement.entity.CategoryEntity;
import com.quocanhit.moneymanagement.entity.ProfileEntity;
import com.quocanhit.moneymanagement.payload.response.BaseResponse;
import com.quocanhit.moneymanagement.repository.ICategoryRepository;
import com.quocanhit.moneymanagement.service.ICategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService {

    private final ProfileService profileService;
    private final ICategoryRepository categoryRepository;

    // Create Category
    @Override
    public ResponseEntity<?> createCategory(CategoryDTO categoryDTO) {
        var profile = profileService.getProfileCurrent();

        if (categoryRepository.existsByNameAndProfileId(categoryDTO.getName(), profile.getId())) {
            return BaseResponse.error("Category already with this name: " + categoryDTO.getName());
        }

        CategoryEntity categoryEntity = toEntity(categoryDTO, profile);
        categoryEntity.setCreatedBy(profile.getFullName());
        categoryEntity.setUpdatedBy(profile.getFullName());

        CategoryDTO response = toDTO(categoryRepository.save(categoryEntity));
        return BaseResponse.success("Create category successfully", response);
    }

    // Get Categories by profile
    @Override
    public ResponseEntity<?> getListCategoryByProfileId() {
        var profile = profileService.getProfileCurrent();
        List<CategoryDTO> response = categoryRepository
                .findCategoryEntitiesByProfileId(profile.getId())
                .stream()
                .map(this::toDTO)
                .toList();

        if (response.isEmpty()) {
            return BaseResponse.error("Category is empty", HttpStatus.NOT_FOUND);
        }

        return BaseResponse.success("Get categories successfully", response);
    }

    // Helper
    private CategoryEntity toEntity(CategoryDTO categoryDTO, ProfileEntity profileEntity) {
        return CategoryEntity.builder()
                .id(categoryDTO.getId())
                .name(categoryDTO.getName())
                .type(categoryDTO.getType())
                .icon(categoryDTO.getIcon())
                .createdAt(categoryDTO.getCreatedAt())
                .createdBy(categoryDTO.getCreatedBy())
                .updatedAt(categoryDTO.getUpdatedAt())
                .updatedBy(categoryDTO.getUpdatedBy())
                .profile(profileEntity)
                .build();
    }

    private CategoryDTO toDTO(CategoryEntity categoryEntity) {
        return CategoryDTO.builder()
                .id(categoryEntity.getId())
                .name(categoryEntity.getName())
                .icon(categoryEntity.getIcon())
                .type(categoryEntity.getType())
                .createdAt(categoryEntity.getCreatedAt())
                .createdBy(categoryEntity.getCreatedBy())
                .updatedAt(categoryEntity.getUpdatedAt())
                .updatedBy(categoryEntity.getUpdatedBy())
                .profileId(categoryEntity.getProfile() != null ? categoryEntity.getProfile().getId() : null)
                .build();
    }
}
