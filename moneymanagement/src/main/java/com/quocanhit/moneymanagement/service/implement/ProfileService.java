package com.quocanhit.moneymanagement.service.implement;

import com.quocanhit.moneymanagement.Enum.EProfileStatus;
import com.quocanhit.moneymanagement.constant.EndpointConst;
import com.quocanhit.moneymanagement.dto.AuthDTO;
import com.quocanhit.moneymanagement.dto.ProfileDTO;
import com.quocanhit.moneymanagement.entity.ProfileEntity;
import com.quocanhit.moneymanagement.payload.response.BaseResponse;
import com.quocanhit.moneymanagement.payload.response.LoginProfileResponse;
import com.quocanhit.moneymanagement.repository.IProfileRepository;
import com.quocanhit.moneymanagement.service.IEmailService;
import com.quocanhit.moneymanagement.service.IProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProfileService implements IProfileService {

    private final IProfileRepository profileRepository;
    private final IEmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Override
    public ProfileDTO registerProfile(ProfileDTO profileDTO) {
        ProfileEntity profileEntity = toEntity(profileDTO);
        profileEntity.setActivationToken(UUID.randomUUID().toString());
        profileEntity.setCreatedBy(profileDTO.getFullName());
        profileEntity.setUpdatedBy(profileDTO.getFullName());
        profileEntity = profileRepository.save(profileEntity);

        // Send activation email
        String activationLink = EndpointConst.BASE_URL_ACTIVATION + profileEntity.getActivationToken();
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
                .password(passwordEncoder.encode(profileDTO.getPassword()))
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
                .imageUrl(profileEntity.getImageUrl())
                .status(profileEntity.getIsActive())
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

    @Override
    public boolean isActiveProfile(String email) {
        return profileRepository.findProfileEntitiesByEmail(email)
                .map(profileEntity -> {
                    profileEntity.getEmail();
                    return true;
                })
                .orElse(false);
    }

    @Override
    public ProfileEntity getProfileCurrent() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return profileRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new UsernameNotFoundException("Profile cannot found with email: " + authentication.getName()));
    }

    @Override
    public ResponseEntity<?> getProfileById(String id) {
        ProfileEntity profile = profileRepository.findProfileEntitiesById(id);
        if (Objects.isNull(toDTO(profile))) {
            return BaseResponse.error("Profile cannot found with ID: " + id, HttpStatus.NOT_FOUND);
        }
        return BaseResponse.success("Successfully", toDTO(profile));
    }

    @Override
    public ProfileDTO getPublishProfile(String email) {
        ProfileEntity profileEntity = null;
        if (email == null) {
            profileEntity = getProfileCurrent();
        } else {
            profileEntity = profileRepository.findByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("Profile cannot found with email: " + email));
        }

        return ProfileDTO.builder()
                .id(profileEntity.getId())
                .fullName(profileEntity.getFullName())
                .email(profileEntity.getEmail())
                .phone(profileEntity.getPhone())
                .address(profileEntity.getAddress())
                .imageUrl(profileEntity.getImageUrl())
                .createdAt(profileEntity.getCreatedAt())
                .createdBy(profileEntity.getCreatedBy())
                .updatedAt(profileEntity.getUpdatedAt())
                .updatedBy(profileEntity.getUpdatedBy())
                .build();
    }

    @Override
    public ResponseEntity<?> authentication(AuthDTO authDTO) {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authDTO.getEmail(), authDTO.getPassword()));

            if (!authentication.isAuthenticated()) {
                return BaseResponse.error("Email or password incorrect", HttpStatus.BAD_REQUEST);
            }

            ProfileEntity profile = profileRepository.findByEmail(authDTO.getEmail())
                    .orElseThrow(() -> new RuntimeException("Profile cannot found"));

            String userId = profile.getId().toString();

            // Generate JWT token
            String accessToken = jwtService.generateToken(profile.getEmail(), userId);
            String refreshToken = jwtService.generateRefreshToken(profile.getEmail(), userId);
            Map<String, Object> tokenData = jwtService.generateTokenWithExpiration(profile.getEmail(), userId);
            Date expirationDate = (Date) tokenData.get("expirationDate");
            LocalDateTime isoExpirationDate = expirationDate.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime();
            LoginProfileResponse response = new LoginProfileResponse(profile, accessToken, refreshToken, isoExpirationDate.toString());
            return BaseResponse.success("Login successfully", response);
        } catch (Exception e) {
            return BaseResponse.error(e.getMessage());
        }
    }
}
