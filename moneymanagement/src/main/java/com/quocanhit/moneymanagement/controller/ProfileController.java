package com.quocanhit.moneymanagement.controller;

import com.quocanhit.moneymanagement.constant.EndpointConst;
import com.quocanhit.moneymanagement.dto.ProfileDTO;
import com.quocanhit.moneymanagement.service.implement.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService _profileService;

    @PostMapping(EndpointConst.USER_REGISTER)
    public ResponseEntity<ProfileDTO> registerProfile(@RequestBody ProfileDTO profileDTO){
        ProfileDTO registerProfile = _profileService.registerProfile(profileDTO);
        return ResponseEntity.ok(registerProfile);
    }

}
