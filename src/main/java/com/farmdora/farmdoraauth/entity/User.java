package com.farmdora.farmdoraauth.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

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
@Table(name = "`user`")
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;

    @Column(length = 50)
    private String id;

    private String pwd;

    @Column(length = 40)
    private String name;

    @Column(length = 40)
    private String email;

    @Column(length = 50)
    private String accountNum;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "postNum", column = @Column(nullable = true)),
            @AttributeOverride(name = "addr", column = @Column(nullable = true)),
            @AttributeOverride(name = "detailAddr", column = @Column(nullable = true))
    })
    private Address address;

    private LocalDate birth;

    @Enumerated(EnumType.ORDINAL)
    private Gender sex;

    @Column(length = 30)
    private String phoneNum;

    @Column(nullable = false)
    private boolean isExpire;

    @Column(nullable = false)
    private boolean isBlind;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "auth_id")
    private Auth auth;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bank_id")
    private BankType bankType;

    public void changeAuth(Auth auth) {
        this.auth = auth;
    }


    public void updateUserInfo(String pwd, String accountNum, LocalDate birth, Address address,
                               String email, Gender sex, String phoneNum, BankType bankType) {
        this.pwd = pwd;
        this.accountNum = accountNum;
        this.birth = birth;
        this.address = address;
        this.email = email;
        this.sex = sex;
        this.phoneNum = phoneNum;
        this.bankType = bankType;
    }

    public void expireUser() {
        this.id = "deleted_user_" + this.userId;
        this.pwd = "";
        this.name = "탈퇴회원";
        this.email = "deleted" + this.userId + "@blind.com";
        this.accountNum = null;
        this.address = null;
        this.birth = null;
        this.sex = null;
        this.phoneNum = null;
        this.isExpire = true;
    }

    public void blindUser() {
        this.isBlind = true;
    }

    public void unblindUser() {
        this.isBlind = false;
    }

}
