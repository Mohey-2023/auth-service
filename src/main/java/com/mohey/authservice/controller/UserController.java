package com.mohey.authservice.controller;

import javax.validation.Valid;

import com.mohey.authservice.dto.*;
import com.mohey.authservice.service.RedisService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.mohey.authservice.dto.DeleteReqDto;
import com.mohey.authservice.service.UserService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
//@RequestMapping("/api")
@RestController
public class UserController {
    private final UserService userService;
    private final RedisService redisService;

    //회원가입 수정
    @PostMapping("/members/auth/join")
    public ResponseEntity<?> join(@RequestBody @Valid JoinReqDto joinReqDto, BindingResult bindingResult) {
        //javax.validation으로 회원가입시 유효성 검사 한다. @Valid 어노테이션으로 joinReqDto에 valid유효성 테스트를 통과하지 못하면
        //bindingResult에 모든 오류의 내용이 담기게 된다
        //BindingResult는 유효성 검사 결과를 저장하는 객체입니다. BindingResult는 @Valid 어노테이션이 적용된 객체의 유효성 검사 결과를 담고 있습니다
        //hasErros로 에러가 있는지 확인 가능
        //여기다가 써도 되지만 AOP로 처리

        JoinRespDto joinRespDto = userService.join(joinReqDto);

        //api호출 멤버서비스에회원가입 하라고 데이터담아서 보내
        // 회원실패시 로직도 만들기
        // 성공하면 프론틍에서성공 메세지일 경우 login 다시 엑세스토큰 담아서 호출 해서 jwt 발급 해주기

        //데이터 두개 각각 다른 주소로 어떻게 보내지...
        //feign 클라이언트 가지고 보내야함!!
        return new ResponseEntity<>(new ResponseDto<>(1, "auth 회원가입 성공", joinRespDto), HttpStatus.CREATED);
    }

    //회원 탈퇴 메서드 추가
    @PostMapping("members/{uuId}/good-bye")
    public ResponseEntity<?> withDraw(@PathVariable String uuId, DeleteReqDto deleteReqDto){
        deleteReqDto.setUuId(uuId);
        userService.withDraw(deleteReqDto);
        return new ResponseEntity<>(new ResponseDto<>(1, "회원탈퇴 성공", " "), HttpStatus.NO_CONTENT);

    }

    //로그아웃
    @PostMapping("/members/logout")
    public ResponseEntity<?> logout(@RequestBody String uuId){
        userService.logout(uuId);
        //System.out.println(redisService.getValues(uuId));
        return new ResponseEntity<>(new ResponseDto<>(1,"로그아웃", ""), HttpStatus.OK);

    }

    //로그인
    //@PostMapping("/members/login")

}