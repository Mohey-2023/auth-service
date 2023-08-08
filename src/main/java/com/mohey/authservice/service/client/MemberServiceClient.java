package com.mohey.authservice.service.client;

import com.mohey.authservice.dto.DeviceUuidRespDto;
import com.mohey.authservice.dto.JoinRespToMembersDto;
import com.mohey.authservice.dto.MemberRespDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Component
@org.springframework.cloud.openfeign.FeignClient(name = "member-service",url = "http://127.0.0.1:8098")
public interface MemberServiceClient {
    //이거 FeignClient에서 jpa 처럼 알아서 구현해준다고 함 !!!
    //내가보내는 거는 postMapping으로 보낼 주소 !!
    @PostMapping("/members/join")
    //ok
    //badRequest
    //resp로 바꿔야할 거같기도...
    //테스트는 반대쪽에 있어야 된다고 함..
    MemberRespDto join(@RequestBody JoinRespToMembersDto joinRespToMembersDto);

    @PostMapping("/members/device/register")
    //created 201
    //badRequest
    MemberRespDto register(@RequestBody DeviceUuidRespDto deviceUuidRespDto);

}