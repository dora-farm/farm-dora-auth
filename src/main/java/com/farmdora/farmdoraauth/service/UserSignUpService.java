package com.farmdora.farmdoraauth.service;

import com.farmdora.farmdoraauth.common.exception.ResourceAlreadyExistsException;
import com.farmdora.farmdoraauth.common.exception.ResourceNotFoundException;
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
            log.info("존재 아이디 {}", user.get().getId());
            throw new ResourceAlreadyExistsException("idCheck", id);
        }
    }

    @Transactional(readOnly = true)
    public void emailCheck(String email){
        Optional<User> user = userRepository.findByEmail(email);
        if(user.isPresent()) {
            throw new ResourceAlreadyExistsException("emailCheck", email);
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
                .orElseThrow(() -> new ResourceNotFoundException("BankType Entity", userSignUpDto.getBankId()));

        userSignUpDto.setBankId(bankType.getId());

        String encodedPwd = bCryptPasswordEncoder.encode(userSignUpDto.getPwd());

        userSignUpDto.setPwd(encodedPwd);

        User user = User.builder()
                .id(userSignUpDto.getId())
                .pwd(userSignUpDto.getPwd())
                .name(userSignUpDto.getName())
                .email(userSignUpDto.getEmail())
                .accountNum(userSignUpDto.getAccountNum())
                .birth(userSignUpDto.getBirth())
                .sex(userSignUpDto.getSex())
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
