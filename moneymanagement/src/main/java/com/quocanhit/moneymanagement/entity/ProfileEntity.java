package com.quocanhit.moneymanagement.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.quocanhit.moneymanagement.Enum.EUserStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_profile")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProfileEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, length = 40)
    private String id;

    @Column(name = "fullName")
    private String fullName;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "phone")
    private String phone;

    @Column(name = "address")
    private String address;

    @JsonIgnore
    @Column(name = "password")
    private String password;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "isActive")
    private EUserStatus isActive;

    @Column(name = "activation_token")
    private String activationToken;

    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "created_by")
    private String createdBy;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "updated_by")
    private String updatedBy;

    @PrePersist
    public void prePersist() {
        if (this.isActive == null) {
            isActive = EUserStatus.INACTIVE;
        }
    }
}
