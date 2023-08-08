package com.mohey.authservice.util;

import javax.servlet.http.HttpServletResponse;

import com.mohey.authservice.dto.DeviceUuidDto;
import com.mohey.authservice.dto.LoginRespDto;
import com.mohey.authservice.service.RedisService;
import com.fasterxml.jackson.databind.JsonNode;
import com.mohey.authservice.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import com.mohey.authservice.dto.ResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;


@Component
public class CustomResponseUtil {
    private static final Logger log = LoggerFactory.getLogger(CustomResponseUtil.class);

    private final RedisService redisService;
    private final UserService userService;

    //의존성주입
    @Autowired
    public CustomResponseUtil(RedisService redisService, UserService userService) {
        this.redisService = redisService;
        this.userService = userService;
    }

    //여기서 토큰 담아보자
    public void success(HttpServletResponse response, Object dto,Object dto2) {
        try {
            ObjectMapper om = new ObjectMapper();
            ResponseDto<?> responseDto = new ResponseDto<>(1, "로그인성공", dto);
            String responseBody = om.writeValueAsString(responseDto);
            response.setContentType("application/json; charset=utf-8");
            response.setStatus(200);
            LoginRespDto loginRespDto = (LoginRespDto) dto;
            DeviceUuidDto deviceUuidDto = (DeviceUuidDto) dto2 ;
            System.out.println("dasddadadadwdadwa"+deviceUuidDto.toString());
            System.out.println("sssddadadadwdadwa"+loginRespDto.getMemberUuid());
                    //uuId를 유저한테 가져와서 줘야하는데....
            //액세스 토큰
            String accessToken = response.getHeader("Authorization");
            //리프레시 토큰
            String refreshToken = response.getHeader("RefreshToken");
            //이거 하나만 나오네..... 어떻게 두개 담지
            //System.out.println("accessToken: " + accessToken);
            //System.out.println("refreshToken: " + refreshToken);
//            System.out.println("responseBody");
//            System.out.println(responseBody);
            //db에 refresh 토큰 저장해야함

            //deviceuuid 검증
            //우리 디비에 있으면 ok
            //없다면 새로 추가 + deviceUuid랑 deviceToken body에 넣어서 넘겨주기
            userService.verifyDeviceUuid(deviceUuidDto.getDeviceUuid(), deviceUuidDto.getDeviceToken(),loginRespDto.getMemberUuid());

            //uuid dto에서 꺼내야함
            String jsonString = responseBody;
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(jsonString);
            String memberUuid = jsonNode.get("data").get("memberUuid").asText();
            System.out.println(memberUuid);
            System.out.println("CustomResponseUtil");
            //System.out.println("responseBody: " + responseBody);
            //System.out.println(memberUuid);
            redisService.setValues(deviceUuidDto.getDeviceUuid() + ':'+ memberUuid,refreshToken);
            //System.out.println("outOfRedis");
            redisService.getValues(deviceUuidDto.getDeviceUuid() + ':'+ memberUuid);

            response.getWriter().println(responseBody); // 예쁘게 메시지를 포장하는 공통적인 응답 DTO를 만들어보자!!
        } catch (Exception e) {
            System.out.println(e.getMessage());
            log.error("서버 파싱 에러");

        }
    }

    public static void fail(HttpServletResponse response, String msg, HttpStatus httpStatus) {
        try {
            ObjectMapper om = new ObjectMapper();
            ResponseDto<?> responseDto = new ResponseDto<>(-1, msg, null);
            String responseBody = om.writeValueAsString(responseDto);
            response.setContentType("application/json; charset=utf-8");
            response.setStatus(httpStatus.value());
            response.getWriter().println(responseBody); // 예쁘게 메시지를 포장하는 공통적인 응답 DTO를 만들어보자!!
        } catch (Exception e) {
            System.out.println(e.getMessage());
            log.error("서버 파싱 에러");
        }
    }
}