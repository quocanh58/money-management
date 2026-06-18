package com.quocanhit.moneymanagement.repository;

import com.quocanhit.moneymanagement.Enum.ECategoryType;
import com.quocanhit.moneymanagement.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ICategoryRepository extends JpaRepository<CategoryEntity, String> {

    List<CategoryEntity> findCategoryEntitiesByProfileId(String profileId);

    CategoryEntity findByIdAndProfileId(String id, String profileId);

    List<CategoryEntity> findByTypeAndProfileId(ECategoryType type, String profileId);

    boolean existsByNameAndProfileId(String name, String profileId);
}
