package com.quocanhit.moneymanagement.dto.category;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.quocanhit.moneymanagement.Enum.ECategoryType;
import com.quocanhit.moneymanagement.entity.ProfileEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryDTO {
    private String id;
    private String name;
    private String icon;
    private ECategoryType type;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDateTime createdAt;
    private String createdBy;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDateTime updatedAt;
    private String updatedBy;
    private ProfileEntity profile;
}
