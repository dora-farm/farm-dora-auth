package com.farmdora.farmdoraauth.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.transaction.annotation.Transactional;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Auth {

    @Id
    @Column(name = "auth_id")
    private Short id;

    private String role;
}
