package com.quocanhit.moneymanagement.controller;

import com.quocanhit.moneymanagement.constant.EndpointConst;
import com.quocanhit.moneymanagement.dto.auth.AuthDTO;
import com.quocanhit.moneymanagement.dto.profile.ProfileDTO;
import com.quocanhit.moneymanagement.payload.response.BaseResponse;
import com.quocanhit.moneymanagement.service.implement.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @PostMapping(EndpointConst.REGISTER)
    public ResponseEntity<?> registerProfile(@RequestBody ProfileDTO profileDTO) {
        ProfileDTO registerProfile = profileService.registerProfile(profileDTO);
        return ResponseEntity.ok(registerProfile);
    }

    @GetMapping(EndpointConst.ACTIVATE_TOKEN)
    public ResponseEntity<?> activationProfile(@RequestParam String token) {
        boolean check = profileService.activationProfile(token);
        if (check) {
            return ResponseEntity.ok("Profile activation successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Activation token note found or already account.");
        }
    }

    @PostMapping(EndpointConst.LOGIN)
    public ResponseEntity<?> login(@RequestBody AuthDTO authDTO) {
        try {
            if (!profileService.isActiveProfile(authDTO.getEmail())) {
                return BaseResponse.error("Profile not yet activation.", HttpStatus.FORBIDDEN);
            }

            return profileService.authentication(authDTO);
        } catch (BadCredentialsException e) {
            return BaseResponse.error("Email or password incorrect.", HttpStatus.UNAUTHORIZED);

        } catch (Exception e) {
            return BaseResponse.error(e.getMessage());
        }
    }

    @GetMapping(EndpointConst.PROFILE_BY_ID)
    public ResponseEntity<?> getProfileById(@PathVariable String id) {
        try {
            return profileService.getProfileById(id);
        } catch (Exception e) {
            return BaseResponse.error(e.getMessage());
        }
    }
}
