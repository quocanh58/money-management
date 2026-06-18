package com.quocanhit.moneymanagement.payload.response;


import com.quocanhit.moneymanagement.Enum.EProfileStatus;
import com.quocanhit.moneymanagement.entity.ProfileEntity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginProfileResponse {
    private Profile profile;
    private String accessToken;
    private String refreshToken;
    private String expiredDate;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Profile {
        private String id;
        private String username;
        private String email;
        private String phone;
        private String address;
        private String imageUrl;
        private String fullName;
        @Enumerated(EnumType.STRING)
        private EProfileStatus isActive;
    }

    public LoginProfileResponse(ProfileEntity userModel, String accessToken, String refreshToken, String expiredDate) {
        this.profile = Profile.builder()
                .id(userModel.getId())
                .email(userModel.getEmail())
                .phone(userModel.getPhone())
                .address(userModel.getAddress())
                .imageUrl(userModel.getImageUrl())
                .fullName(userModel.getFullName())
                .isActive(userModel.getIsActive())
                .build();
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.expiredDate = expiredDate;
    }
}
