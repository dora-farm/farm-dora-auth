package com.farmdora.farmdoraauth.mypage.user.depot.controller;

import com.farmdora.farmdoraauth.common.response.HttpResponse;
import com.farmdora.farmdoraauth.entity.User;
import com.farmdora.farmdoraauth.jwt.JwtUtil;
import com.farmdora.farmdoraauth.mypage.user.depot.dto.DepotModifyRequestDto;
import com.farmdora.farmdoraauth.mypage.user.depot.dto.DepotRegisterRequestDto;
import com.farmdora.farmdoraauth.mypage.user.depot.dto.DepotSelectResponseDto;
import com.farmdora.farmdoraauth.mypage.user.depot.dto.UserAddressDto;
import com.farmdora.farmdoraauth.mypage.user.depot.message.DepotMassage;
import com.farmdora.farmdoraauth.mypage.user.depot.service.DepotService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/mypage/user/depot")
public class DepotRestController {

    private final DepotService depotService;

    @GetMapping("/all")
    public HttpResponse getDepotById(Principal principal) {

        Integer userId = Integer.parseInt(principal.getName());
        try {
            List<DepotSelectResponseDto> depotList = depotService.getDepotsByUserId(userId);
            return HttpResponse.builder()
                    .status(200)
                    .message(DepotMassage.DEPOT_GET_ALL_SUCCESS.getMessage())
                    .data(depotList)
                    .build();
        }catch (Exception e) {
            return HttpResponse.builder()
                    .status(200)
                    .message(null)
                    .data(null)
                    .build();
        }
    }

    @GetMapping("/user/address")
    public HttpResponse getDepotAddressById(Principal principal) {

        Integer userId = Integer.parseInt(principal.getName());
        try {
            UserAddressDto userAddr = depotService.getUserAddr(userId);
            log.info("ddd {}",userAddr.toString());
        return HttpResponse.builder()
                .status(200)
                .message(DepotMassage.USER_ADDRESS_GET_SUCCESS.getMessage())
                .data(userAddr)
                .build();
        }catch (Exception e){
            log.info("ddd {}",userId);
            return HttpResponse.builder()
                    .status(200)
                    .message(DepotMassage.USER_ADDRESS_GET_FAILURE.getMessage())
                    .data(null)
                    .build();
        }
    }

    @GetMapping("/detail/{depotId}")
    public HttpResponse getDetailDepotById(@PathVariable int depotId) {
        DepotSelectResponseDto depot = depotService.getDepotById(depotId);
        return HttpResponse.builder()
                .status(200)
                .message(DepotMassage.DEPOT_GET_SUCCESS.getMessage())
                .data(depot)
                .build();
    }

    @PostMapping("/register")
    public HttpResponse saveDepot(Principal principal ,@RequestBody DepotRegisterRequestDto registerRequest) {
        try {
//            String token = JwtFromCookie.extractTokenFromCookie(request);
//            int userId = jwtUtil.getUserId(token);
//
            Integer userId = Integer.parseInt(principal.getName());
            registerRequest.setUserId(userId);
            depotService.registerDepot(registerRequest);

            return HttpResponse.builder()
                    .status(200)
                    .message(DepotMassage.DEPOT_REGISTER_SUCCESS.getMessage())
                    .data(true)
                    .build();
        } catch (Exception e) {
            return HttpResponse.builder()
                    .status(200)
                    .message(DepotMassage.DEPOT_REGISTER_FAILURE.getMessage())
                    .data(false)
                    .build();
        }
    }

    @PutMapping("/modify")
    public HttpResponse modifyDepot(@RequestBody DepotModifyRequestDto modifyRequest) {

        try {
            depotService.modifyDepot(modifyRequest);
            return HttpResponse.builder()
                    .status(200)
                    .message(DepotMassage.DEPOT_MODIFY_SUCCESS.getMessage())
                    .data(true)
                    .build();
        } catch (Exception e) {
            return HttpResponse.builder()
                    .status(200)
                    .message(DepotMassage.DEPOT_MODIFY_FAILURE.getMessage())
                    .data(false)
                    .build();
        }
    }

    @DeleteMapping("/delete/{depotId}")
    public HttpResponse deleteDepot(@PathVariable int depotId) {
        try {
            depotService.deleteDepot(depotId);
            return HttpResponse.builder()
                    .status(200)
                    .message(DepotMassage.DEPOT_DELETE_SUCCESS.getMessage())
                    .data(true)
                    .build();
        } catch (Exception e) {
            return HttpResponse.builder()
                    .status(200)
                    .message(DepotMassage.DEPOT_DELETE_FAILURE.getMessage())
                    .data(false)
                    .build();
        }
    }
}
