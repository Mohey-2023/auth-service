package com.mohey.authservice.controller;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.mohey.authservice.config.auth.LoginUser;
import com.mohey.authservice.config.jwt.JwtProcess;
import com.mohey.authservice.config.jwt.JwtVO;
import com.mohey.authservice.dto.*;
import com.mohey.authservice.service.ImageService;
import com.mohey.authservice.service.RedisService;
import feign.FeignException;
import feign.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.mohey.authservice.dto.DeleteReqDto;
import com.mohey.authservice.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.rmi.ServerException;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
//@RequestMapping("/api")
@RestController
@RequestMapping("/auth")

public class UserController {
    private final UserService userService;
    private final RedisService redisService;
    private final ImageService imageService;


    //회원가입 수정
    @PostMapping("/join")
    public ResponseEntity<?> join(@RequestBody JoinReqDto joinReqDto, BindingResult bindingResult) {
        //javax.validation으로 회원가입시 유효성 검사 한다. @Valid 어노테이션으로 joinReqDto에 valid유효성 테스트를 통과하지 못하면
        //bindingResult에 모든 오류의 내용이 담기게 된다
        //BindingResult는 유효성 검사 결과를 저장하는 객체입니다. BindingResult는 @Valid 어노테이션이 적용된 객체의 유효성 검사 결과를 담고 있습니다
        //hasErros로 에러가 있는지 확인 가능
        //여기다가 써도 되지만 AOP로 처리
        // System.out.println(joinReqDto);
        log.info(joinReqDto.toString());
        JoinRespDto joinRespDto;
        try {
            joinRespDto = userService.join(joinReqDto);
            return new ResponseEntity<>(new ResponseDto<>(1, "auth 회원가입 성공", joinRespDto), HttpStatus.CREATED);
        } catch (ServerException e) {
            //로그인으로 보내
            //어떻게 .... ? 카카오 로그인으로 보내야 하잖아
            // 프론트에서 리다이렉트 0
            return new ResponseEntity<>(new ResponseDto<>(0, e.getMessage(), " "), HttpStatus.CONFLICT);
            //throw new RuntimeException(e);
        }

        //api호출 멤버서비스에 회원가입 하라고 데이터담아서 보내
        // 회원실패시 로직도 만들기
        // 성공하면 프론틍에서성공 메세지일 경우 login 다시 엑세스토큰 담아서 호출 해서 jwt 발급 해주기

        //데이터 두개 각각 다른 주소로 어떻게 보내지...
        //feign 클라이언트 가지고 보내야함!!

    }

    //회원 탈퇴 메서드 추가
    @PostMapping("/{uuId}/good-bye")
    public ResponseEntity<?> withDraw(@PathVariable String memberUuid, DeleteReqDto deleteReqDto) {
        deleteReqDto.setMemberUuid(memberUuid);
        userService.withDraw(deleteReqDto);
        return new ResponseEntity<>(new ResponseDto<>(1, "회원탈퇴 성공", " "), HttpStatus.NO_CONTENT);
    }

    //로그아웃
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody String memberUuId) {
        userService.logout(memberUuId);
        //System.out.println(redisService.getValues(uuId));
        return new ResponseEntity<>(new ResponseDto<>(1, "로그아웃", ""), HttpStatus.OK);

    }

    @PostMapping("/jwt/verifyRefreshToken")
    public ResponseEntity<?> isVerifiedRefreshToken(@RequestHeader String RefreshToken) {

        //System.out.println("inputrefresh: "+ RefreshToken);
        Boolean refresh = userService.verifyRefreshToken(RefreshToken);
        //System.out.println();

        if (refresh) {
            return new ResponseEntity<>(new ResponseDto<>(1, "refreshToken 유효", null), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ResponseDto<>(-1, "refreshToken 유효하지 않거나 올바른 토큰이 아닙니다.", null), HttpStatus.BAD_REQUEST);
        }
    }


    @PostMapping("image/request") // s3 url 요청
    public ResponseEntity<String> requestUrl(@RequestBody Map<String, String> payload) {
        String fileName = payload.get("fileName");
        String imageType = payload.get("imageType");
        log.info(fileName);
        log.info(imageType);


        try {
            String presignedUrl =imageService.requestUrl(fileName, imageType);
            return ResponseEntity.ok(presignedUrl);
        } catch (Exception e) {
            // log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error generating pre-signed URL");
        }

    }

}