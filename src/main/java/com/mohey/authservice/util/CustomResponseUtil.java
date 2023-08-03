package com.mohey.authservice.util;

import javax.servlet.http.HttpServletResponse;

import com.mohey.authservice.service.RedisService;
import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import com.mohey.authservice.dto.ResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.UUID;


@Component
public class CustomResponseUtil {
    private static final Logger log = LoggerFactory.getLogger(CustomResponseUtil.class);


    private final RedisService redisService;

    //의존성주입
    @Autowired
    public CustomResponseUtil(RedisService redisService) {
        this.redisService = redisService;
    }

    //여기서 토큰 담아보자
    public void success(HttpServletResponse response, Object dto) {
        try {
            ObjectMapper om = new ObjectMapper();
            ResponseDto<?> responseDto = new ResponseDto<>(1, "로그인성공", dto);
            String responseBody = om.writeValueAsString(responseDto);
            response.setContentType("application/json; charset=utf-8");
            response.setStatus(200);

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

            //uuid dto에서 꺼내야함
            String jsonString = responseBody;
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(jsonString);
            String uuId = jsonNode.get("data").get("uuId").asText();
            System.out.println(uuId);
            //System.out.println("responseBody: " + responseBody);
            //System.out.println(uuid);
            redisService.setValues(uuId,refreshToken);
            //System.out.println("outOfRedis");
            redisService.getValues(uuId);

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