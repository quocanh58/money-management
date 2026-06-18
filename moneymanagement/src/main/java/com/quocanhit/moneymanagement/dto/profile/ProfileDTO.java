package com.quocanhit.moneymanagement.dto.profile;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.quocanhit.moneymanagement.Enum.EProfileStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileDTO {
    private String id;
    private String fullName;
    private String email;
    private String phone;
    private String address;
    private EProfileStatus status;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    private String imageUrl;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDateTime createdAt;
    private String createdBy;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDateTime updatedAt;
    private String updatedBy;

}
