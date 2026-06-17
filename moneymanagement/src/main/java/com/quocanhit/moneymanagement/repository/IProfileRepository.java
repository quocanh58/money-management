package com.quocanhit.moneymanagement.repository;

import com.quocanhit.moneymanagement.entity.ProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IProfileRepository extends JpaRepository<ProfileEntity, String> {

    // select * from tbl_profile where email = ?
    Optional<ProfileEntity> findByEmail(String email);

    // select * from tbl_profile where activation_token = ?
    Optional<ProfileEntity> findByActivationToken(String activationToken);
}
