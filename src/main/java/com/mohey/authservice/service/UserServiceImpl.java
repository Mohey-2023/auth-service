package com.mohey.authservice.service;

import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.mohey.authservice.config.auth.LoginUser;
import com.mohey.authservice.config.jwt.JwtVO;
import com.mohey.authservice.domain.UserDeviceInfo;
import com.mohey.authservice.domain.UserEnum;
import com.mohey.authservice.domain.UserWithdrawal;
import com.mohey.authservice.dto.*;
import com.mohey.authservice.repository.UserDeviceInfoRepository;
import com.mohey.authservice.repository.UserRepository;
import com.mohey.authservice.repository.UserWithdrawalRepository;
import com.mohey.authservice.service.client.MemberServiceClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mohey.authservice.domain.User;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.client.HttpServerErrorException;

import java.rmi.ServerException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@Slf4j
public class UserServiceImpl implements UserService {
    private final RedisService redisService;
    private final ImageService imageService;


    private final Logger log = LoggerFactory.getLogger(getClass());
    private final UserRepository userRepository;
    private final UserWithdrawalRepository userWithdrawalRepository;
    private final KaKaoLoginService kakaoLoginService;
    private final MemberServiceClient memberServiceClient;
    private final UserDeviceInfoRepository userDeviceInfoRepository;


    // 서비스는 DTO를 요청받고, DTO로 응답한다.
    @Transactional // 트랜잭션이 메서드 시작할 때, 시작되고, 종료될 때 함께 종료
    public JoinRespDto join(JoinReqDto joinReqDto) throws ServerException {
        //S3에 이미지 url 가지고 오기
        String imageUrl = imageService.getImageUrl(joinReqDto.getProfileUrl());
        log.info(imageUrl);

        //db있는지 확인!
        String kakaoId = kakaoLoginService.createKakaoUser(joinReqDto.getAccessToken());
        //System.out.println(userRepository.findByUsername(kakaoId));
        //.isPresent() empty면 false 값이 있으면 true
        if(userRepository.findByKakaoId(kakaoId).isPresent()){
            //이미 있는 사용자니까 로그인하라고 하기
            throw new ServerException("이미 가입된 사용자입니다.");

        }
        String memberUuid = UUID.randomUUID().toString();
        String deviceUuid = UUID.randomUUID().toString();

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String password = passwordEncoder.encode("");
        System.out.println("deviceUuid: "+deviceUuid);

        //액세스토큰으로 카카오 아이디 받아와서 객체 만드는 걸로 변경
        userRepository.save(new User(kakaoId, memberUuid,password, UserEnum.MEMBER, LocalDateTime.now()));


        //여기서 멤버한테 보내야 함. .. .!!
        //dto만들어주고
        //client에 있는 메서드 불러오기
        //System.out.println("joinfeign1");
        memberServiceClient.join(new JoinRespToMembersDto(memberUuid,joinReqDto.getBirthDate(),joinReqDto.getGender(),deviceUuid,joinReqDto.getDeviceToken(),joinReqDto.getSelfIntroduction(), imageUrl, joinReqDto.getInterests(), joinReqDto.getNickname()));
        //System.out.println("joinfeign2");

        // 3. dto 응답
        //잘 된 경우
//        if (memberJoinSuccess) {
//            return new JoinRespDto(userPS);
//        }

        //System.out.println("멤버 정보 넘겨주기 실패 !");
        return new JoinRespDto(memberUuid);
    }

    //로그아웃, 회원탈퇴

    public void withDraw(DeleteReqDto deleteReqDto) {

        //회원 탈퇴 상태만 바꿔서 다시 포스트로 쏘기
        User user = userRepository.findByMemberUuid(deleteReqDto.getMemberUuid());
        //status 변경해서 userWithdrawal 만들어주기

        UserWithdrawal userWithdrawal = UserWithdrawal.builder()
                .userId(user)
                .build();
        userWithdrawalRepository.save(userWithdrawal);
        //리턴 필요 x
    }

    public void logout(String memberUuid){
        //리프레시토큰을 만료시키기
        redisService.expireValues(memberUuid);
    }

    @Override
    public Boolean verifyRefreshToken(String refreshToken) {
        try{
            //PREFIX는 인코딩 안된거라 빼줘야됨
            String realRefresh = refreshToken.substring(JwtVO.TOKEN_PREFIX.length());
            //System.out.println(realRefresh);
            DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC512(JwtVO.SECRET)).build().verify(realRefresh);
            //System.out.println(decodedJWT);
            String memberUuid = decodedJWT.getClaim("memberUuid").asString(); //토큰 내부는 암호화 안되어 있으니까 최소한의 정보만 넣자
            String deviceUuid = decodedJWT.getClaim("deviceUuid").asString();
            //System.out.println(memberUuid);
            //System.out.println(deviceUuid);
            //redisService.getValuse(deviceUuid+':'+memberUuid)로 찾아야 나옴... 이걸 어떻게 가져오지
            //redisService.getValues(memberUuid)
            //db에 저장된 거랑 같아 ??
            if (redisService.getValues(deviceUuid+':'+memberUuid) == null) {
                return false;
            } else {
                return true;
                //IllegalArgumentException, HttpServerErrorException.InternalServerError
            }} catch (Exception e){
            return false;
        }

    }

    @Override
    public void verifyDeviceUuid(String deviceUuid,String deviceToken, String memberUuid) {
        UserDeviceInfo userDeviceInfo = userDeviceInfoRepository.findByDeviceUuid(deviceUuid);
        System.out.println("verifyDeviceUuid :"+ userDeviceInfo);
        if(userDeviceInfo == null){
            User user = userRepository.findByMemberUuid(memberUuid);
            UserDeviceInfo userDeviceInfo1 = new UserDeviceInfo(user,deviceUuid);
            userDeviceInfoRepository.save(userDeviceInfo1);

            DeviceUuidRespDto deviceUuidRespDto = new DeviceUuidRespDto(memberUuid, deviceUuid, deviceToken);
            //fein 처리
            //System.out.println("ljlkjlkljkljk");
            memberServiceClient.register(deviceUuidRespDto);
            //System.out.println("qwerqwer");
            //System.out.println("feign: "+memberServiceClient.register(deviceUuidRespDto));

            //status에 따라 재요청
        }}
}