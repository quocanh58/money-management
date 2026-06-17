package com.quocanhit.moneymanagement.service;

import com.quocanhit.moneymanagement.dto.ProfileDTO;
import com.quocanhit.moneymanagement.entity.ProfileEntity;
import org.springframework.stereotype.Service;

@Service
public interface IProfileService {
    ProfileDTO registerProfile(ProfileDTO profileDTO);

    ProfileEntity toEntity(ProfileDTO profileDTO);

    ProfileDTO toDTO(ProfileEntity profileEntity);

    boolean activationProfile(String activationToken);
}
