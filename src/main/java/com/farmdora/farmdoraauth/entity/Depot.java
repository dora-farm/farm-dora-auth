package com.farmdora.farmdoraauth.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Depot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "depot_id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    @Column(nullable = false, length = 50)
    private String deliveryName;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, length = 30)
    private String phoneNum;

    @Embedded
    private Address address;

    @Column(name = "`require`")
    private String require;

    @Column(nullable = false)
    private boolean isDefault;

    public void updateInfo(String deliveryName, String receiverName, String phoneNum,
                           Address address, String require, boolean defaultAddr) {
        this.deliveryName = deliveryName;
        this.name = receiverName;
        this.phoneNum = phoneNum;
        this.address = address;
        this.require = require;
        this.isDefault = defaultAddr;
    }
}
