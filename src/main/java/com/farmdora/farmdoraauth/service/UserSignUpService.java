package com.farmdora.farmdoraauth.service;

import com.farmdora.farmdoraauth.customException.BankTypeNotFoundException;
import com.farmdora.farmdoraauth.dto.UserSignUpDto;
import com.farmdora.farmdoraauth.entity.*;
import com.farmdora.farmdoraauth.repository.BankTypeRepository;
import com.farmdora.farmdoraauth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Random;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserSignUpService {

    private final UserRepository userRepository;
    private final BankTypeRepository bankTypeRepository;
    private final EmailRedisService emailRedisService;
    private final EmailSendService emailSendService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional(readOnly = true)
    public void idCheck(String id) {
        Optional<User> user = userRepository.findById(id);

        if (user.isPresent()) {
            User userEntity = user.get();
            System.out.println("유저"+user);
            System.out.println("권한"+userEntity);
            throw new RuntimeException("이미 존재하는 아이디입니다.");
        }
    }

    @Transactional(readOnly = true)
    public void emailCheck(String email){
        Optional<User> user = userRepository.findByEmail(email);
        if(user.isPresent()) {
            throw new RuntimeException("이미 존재하는 이메일입니다.");
        }
    }

    public void sendVerificationEmail(String email){
        String code = String.valueOf(new Random().nextInt(999999));
        emailRedisService.saveVerificationCode(email, code);
        emailSendService.sendEmail(email,"이메일 인증 요청",code);
    }

    public boolean verifyEmail(String email, String code){
        boolean isValid = emailRedisService.verifyCode(email, code);
        if(isValid){
            emailRedisService.deleteVerificationCode(email);
        }
        return isValid;
    }

    public boolean registerUser(UserSignUpDto userSignUpDto){
        userSignUpDto.setAuthId((short) 3);

        BankType bankType = bankTypeRepository.findById(userSignUpDto.getBankId())
                .orElseThrow(() -> new BankTypeNotFoundException());

        userSignUpDto.setBankId(bankType.getId());

        String encodedPwd = bCryptPasswordEncoder.encode(userSignUpDto.getPwd());

        log.info("PWD: " + encodedPwd);

        userSignUpDto.setPwd(encodedPwd);

        User user = User.builder()
                .id(userSignUpDto.getId())
                .pwd(userSignUpDto.getPwd())
                .name(userSignUpDto.getName())
                .email(userSignUpDto.getEmail())
                .accountNum(userSignUpDto.getAccountNum())
                .birth(userSignUpDto.getBirth())
                .sex(userSignUpDto.getSex().getValue())
                .phoneNum(userSignUpDto.getPhoneNum())
                .bankType(bankType)
                .auth(Auth.builder()
                        .id(userSignUpDto.getAuthId())
                        .build())
                .isExpire(false)
                .isBlind(false)
                .build();

        try {
            userRepository.save(user);
            return true;
        } catch (Exception e) {
            return false;
        }
    };
}
