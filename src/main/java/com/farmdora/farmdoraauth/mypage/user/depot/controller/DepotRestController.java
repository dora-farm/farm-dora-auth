package com.farmdora.farmdoraauth.mypage.user.depot.controller;

import com.farmdora.farmdoraauth.common.response.HttpResponse;
import com.farmdora.farmdoraauth.mypage.user.depot.dto.DepotDeleteDto;
import com.farmdora.farmdoraauth.mypage.user.depot.dto.DepotDto;
import com.farmdora.farmdoraauth.mypage.user.depot.message.DepotMassage;
import com.farmdora.farmdoraauth.mypage.user.depot.service.DepotService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/mypage/depot")
public class DepotRestController {

    private final DepotService depotService;

    @GetMapping("/{id}")
    public HttpResponse getDepotById(@PathVariable("id") String username) {
        return HttpResponse.builder().build();
    }

    @PostMapping("/register")
    public HttpResponse saveDepot(@RequestBody DepotDto registerRequest) {
        try {
            depotService.saveDepot(registerRequest);
            return HttpResponse.builder()
                    .status(200)
                    .message(DepotMassage.DEPOT_MODIFY_SUCCESS.getMessage())
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
    public HttpResponse modifyDepot(@RequestBody DepotDto modifyRequest) {
        try {
            depotService.saveDepot(modifyRequest);
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

    @DeleteMapping("/delete")
    public HttpResponse deleteDepot(@RequestBody DepotDeleteDto deleteRequest) {
        try {
            depotService.deleteDepot(deleteRequest);
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
