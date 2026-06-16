package com.quocanhit.moneymanagement.service;

import com.quocanhit.moneymanagement.dto.ProfileDTO;
import com.quocanhit.moneymanagement.entity.ProfileEntity;
import org.springframework.stereotype.Service;

@Service
public interface IProfileService {
    public ProfileDTO registerProfile(ProfileDTO profileDTO);

    public ProfileEntity toEntity(ProfileDTO profileDTO);

    public ProfileDTO toDTO(ProfileEntity profileEntity) ;
}
