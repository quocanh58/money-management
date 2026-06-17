package com.quocanhit.moneymanagement.controller;

import com.quocanhit.moneymanagement.Enum.EProfileStatus;
import com.quocanhit.moneymanagement.constant.EndpointConst;
import com.quocanhit.moneymanagement.dto.ProfileDTO;
import com.quocanhit.moneymanagement.service.implement.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @PostMapping(EndpointConst.USER_REGISTER)
    public ResponseEntity<ProfileDTO> registerProfile(@RequestBody ProfileDTO profileDTO) {
        ProfileDTO registerProfile = profileService.registerProfile(profileDTO);
        return ResponseEntity.ok(registerProfile);
    }

    @GetMapping(EndpointConst.USER_ACTIVATE_TOKEN)
    public ResponseEntity<String> activationProfile(@RequestParam String token) {
        boolean check = profileService.activationProfile(token);
        if (check) {
            return ResponseEntity.ok("Profile activation successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Activation token note found or already account.");
        }
    }
}
