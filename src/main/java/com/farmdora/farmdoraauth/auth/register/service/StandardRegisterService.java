package com.farmdora.farmdoraauth.auth.register.service;

import com.farmdora.farmdoraauth.common.exception.ResourceAlreadyExistsException;
import com.farmdora.farmdoraauth.common.exception.ResourceNotFoundException;
import com.farmdora.farmdoraauth.auth.register.dto.StandardRegisterDto;
import com.farmdora.farmdoraauth.entity.*;
import com.farmdora.farmdoraauth.auth.register.repository.BankTypeRepository;
import com.farmdora.farmdoraauth.auth.register.repository.UserRepository;
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
public class StandardRegisterService {

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
    public void emailCheck(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            throw new ResourceAlreadyExistsException("emailCheck", email);
        }
    }

    public void sendVerificationEmail(String email, String sub, String title, String content) {
        String code = String.valueOf(new Random().nextInt(999999));
        emailRedisService.saveVerificationCode(email, code);
        emailSendService.sendEmail(email, sub, code , title, content);
    }

    public boolean verifyEmail(String email, String code) {
        boolean isValid = emailRedisService.verifyCode(email, code);
        if (isValid) {
            emailRedisService.deleteVerificationCode(email);
        }
        return isValid;
    }

    public boolean registerUser(StandardRegisterDto standardRegisterDto) {
        standardRegisterDto.setAuthId((short) 3);

        BankType bankType = bankTypeRepository.findById(standardRegisterDto.getBankId())
                .orElseThrow(() -> new ResourceNotFoundException("BankType Entity", standardRegisterDto.getBankId()));

        standardRegisterDto.setBankId(bankType.getId());

        String encodedPwd = bCryptPasswordEncoder.encode(standardRegisterDto.getPwd());

        standardRegisterDto.setPwd(encodedPwd);

        User user = User.builder()
                .id(standardRegisterDto.getId())
                .pwd(standardRegisterDto.getPwd())
                .name(standardRegisterDto.getName())
                .email(standardRegisterDto.getEmail())
                .accountNum(standardRegisterDto.getAccountNum())
                .birth(standardRegisterDto.getBirth())
                .sex(standardRegisterDto.getSex())
                .phoneNum(standardRegisterDto.getPhoneNum())
                .bankType(bankType)
                .auth(Auth.builder()
                        .id(standardRegisterDto.getAuthId())
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
    }
}
