package com.farmdora.farmdoraauth.mypage.user.depot.service;

import com.farmdora.farmdoraauth.auth.register.repository.UserRepository;
import com.farmdora.farmdoraauth.entity.Address;
import com.farmdora.farmdoraauth.entity.Depot;
import com.farmdora.farmdoraauth.entity.User;
import com.farmdora.farmdoraauth.mypage.user.depot.dto.DepotDeleteDto;
import com.farmdora.farmdoraauth.mypage.user.depot.dto.DepotDto;
import com.farmdora.farmdoraauth.mypage.user.depot.repository.DepotRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class DepotService {
    private final DepotRepository depotRepository;
    private final UserRepository userRepository;


    public void saveDepot(DepotDto depotRegisterDto) {
        Depot depot = Depot.builder()
                .user(User.builder()
                        .userId(depotRegisterDto.getUserId())
                        .build()
                )
                .deliveryName(depotRegisterDto.getDeliveryName())
                .name(depotRegisterDto.getReceiverName())
                .phoneNum(depotRegisterDto.getPhoneNum())
                .user(User.builder()
                        .address(Address.builder()
                                .postNum(depotRegisterDto.getPostNum())
                                .addr(depotRegisterDto.getAddr())
                                .detailAddr(depotRegisterDto.getDetailAddr())
                                .build())
                        .build())
                .require(depotRegisterDto.getRequire())
                .isDefault(depotRegisterDto.isDefaultAddr())
                .build();
        depotRepository.save(depot);
        userRepository.save(depot.getUser());
    }
    public void deleteDepot(DepotDeleteDto deleteRequest) {
        depotRepository.deleteById(deleteRequest.getDepotId());
    }

}
