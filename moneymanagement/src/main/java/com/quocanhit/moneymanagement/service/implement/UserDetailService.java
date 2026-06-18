package com.quocanhit.moneymanagement.service.implement;

import com.quocanhit.moneymanagement.entity.ProfileEntity;
import com.quocanhit.moneymanagement.repository.IProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class UserDetailService implements UserDetailsService {

    private final IProfileRepository profileRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        ProfileEntity profileEntityExist = profileRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Profile cannot found with email: " + email));

        return User.builder()
                .username(profileEntityExist.getEmail())
                .password(profileEntityExist.getPassword())
                .authorities(Collections.emptyList())
                .build();
    }
}
