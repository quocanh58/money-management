package com.quocanhit.moneymanagement.service.implement;

import com.quocanhit.moneymanagement.Enum.EProfileStatus;
import com.quocanhit.moneymanagement.dto.ProfileDTO;
import com.quocanhit.moneymanagement.entity.ProfileEntity;
import com.quocanhit.moneymanagement.repository.IProfileRepository;
import com.quocanhit.moneymanagement.service.IEmailService;
import com.quocanhit.moneymanagement.service.IProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProfileService implements IProfileService {

    private final IProfileRepository profileRepository;
    private final IEmailService emailService;

    @Override
    public ProfileDTO registerProfile(ProfileDTO profileDTO) {
        ProfileEntity profileEntity = toEntity(profileDTO);
        profileEntity.setActivationToken(UUID.randomUUID().toString());
        profileEntity.setCreatedBy(profileDTO.getFullName());
        profileEntity.setUpdatedBy(profileDTO.getFullName());
        profileEntity = profileRepository.save(profileEntity);

        // send activation email
        String activationLink = "http://localhost:8080/api/v1/profile/active?token=" + profileEntity.getActivationToken();
        String subject = "Activation your Money Management Account";
        String body = "Click on the following link to activation your account: " + activationLink;
        emailService.sendEmail(profileEntity.getEmail(), subject, body);

        return toDTO(profileEntity);
    }

    @Override
    public ProfileEntity toEntity(ProfileDTO profileDTO) {
        return ProfileEntity.builder()
                .id(profileDTO.getId())
                .fullName(profileDTO.getFullName())
                .email(profileDTO.getEmail())
                .password(profileDTO.getPassword())
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
                .password(profileEntity.getPassword())
                .email(profileEntity.getEmail())
                .imageUrl(profileEntity.getImageUrl())
                .createdAt(profileEntity.getCreatedAt())
                .createdBy(profileEntity.getCreatedBy())
                .updatedAt(profileEntity.getUpdatedAt())
                .updatedBy(profileEntity.getUpdatedBy())
                .build();
    }

    @Override
    public boolean activationProfile(String activationToken) {
        return profileRepository.findByActivationToken(activationToken)
                .map(profile -> {
                    profile.setIsActive(EProfileStatus.ACTIVE);
                    profileRepository.save(profile);
                    return true;
                }).orElse(false);
    }
}
