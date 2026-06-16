package com.quocanhit.moneymanagement.service.implement;

import com.quocanhit.moneymanagement.dto.ProfileDTO;
import com.quocanhit.moneymanagement.entity.ProfileEntity;
import com.quocanhit.moneymanagement.repository.IProfileRepository;
import com.quocanhit.moneymanagement.service.IProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProfileService implements IProfileService {

    private final IProfileRepository _profileRepository;


    @Override
    public ProfileDTO registerProfile(ProfileDTO profileDTO) {
        ProfileEntity profileEntity = toEntity(profileDTO);
        profileEntity.setActivationToken(UUID.randomUUID().toString());
        profileEntity.setCreatedBy(profileDTO.getFullName());
        profileEntity.setUpdatedBy(profileDTO.getFullName());

        profileEntity = _profileRepository.save(profileEntity);

        return toDTO(profileEntity);
    }

    @Override
    public ProfileEntity toEntity(ProfileDTO profileDTO) {
        return ProfileEntity.builder()
                .id(profileDTO.getId())
                .fullName(profileDTO.getFullName())
                .password(profileDTO.getPassword())
                .email(profileDTO.getEmail())
                .phone(profileDTO.getPhone())
                .address(profileDTO.getAddress())
                .imageUrl(profileDTO.getImageUrl())
                .createdAt(profileDTO.getCreatedAt())
                .createdBy(profileDTO.getCreatedBy())
                .updatedAt(profileDTO.getUpdatedAt())
                .updatedBy(profileDTO.getUpdatedBy())
                .build();
    }

    @Override
    public ProfileDTO toDTO(ProfileEntity profileEntity) {
        return ProfileDTO.builder()
                .id(profileEntity.getId())
                .fullName(profileEntity.getFullName())
                .phone(profileEntity.getPhone())
                .address(profileEntity.getAddress())
                .email(profileEntity.getEmail())
//                .password(profileEntity.getPassword()) // no display password
                .imageUrl(profileEntity.getImageUrl())
                .createdAt(profileEntity.getCreatedAt())
                .createdBy(profileEntity.getCreatedBy())
                .updatedAt(profileEntity.getUpdatedAt())
                .updatedBy(profileEntity.getUpdatedBy())
                .build();
    }
}
