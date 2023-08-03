package com.mohey.authservice.service;

import com.mohey.authservice.config.auth.LoginService;
import com.mohey.authservice.domain.UserWithdrawal;
import com.mohey.authservice.domain.UserWithdrawalRepository;
import com.mohey.authservice.dto.DeleteReqDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mohey.authservice.domain.User;
import com.mohey.authservice.domain.UserRepository;
import com.mohey.authservice.dto.JoinReqDto;
import com.mohey.authservice.dto.JoinRespDto;

import lombok.RequiredArgsConstructor;

import java.rmi.ServerException;
import java.util.Optional;


@RequiredArgsConstructor
@Service
public class UserService {
    private final Logger log = LoggerFactory.getLogger(getClass());
    private final UserRepository userRepository;
    private final UserWithdrawalRepository userWithdrawalRepository;
    private final RedisService redisService;
    private final KaKaoLoginService kakaoLoginService;

    // 서비스는 DTO를 요청받고, DTO로 응답한다.
    @Transactional // 트랜잭션이 메서드 시작할 때, 시작되고, 종료될 때 함께 종료
    public JoinRespDto join(JoinReqDto joinReqDto) throws ServerException {
        //db있는지 확인!
        String kakaoId = kakaoLoginService.createKakaoUser(joinReqDto.getAccessToken());
        //System.out.println(userRepository.findByUsername(kakaoId));
        //.isPresent() empty면 false 값이 있으면 true
        if(userRepository.findByUsername(kakaoId).isPresent()){
            //이미 있는 사용자니까 로그인하라고 하기
            throw new ServerException("이미 가입된 사용자입니다.");
        //수정...해야댐
        }

        joinReqDto.setUsername(kakaoId);
        //액세스토큰으로 카카오 아이디 받아와서 객체 만드는 걸로 변경
        User userPS = userRepository.save(joinReqDto.toEntity());

        // 3. dto 응답
        return new JoinRespDto(userPS);
    }

    //로그아웃, 회원탈퇴

    public void withDraw(DeleteReqDto deleteReqDto) {

        //회원 탈퇴 상태만 바꿔서 다시 포스트로 쏘기
        User user = userRepository.findByUuId(deleteReqDto.getUuId()).get();
        //status 변경해서 userWithdrawal 만들어주기

        UserWithdrawal userWithdrawal = UserWithdrawal.builder()
                .userId(user.getId())
                .build();
        userWithdrawalRepository.save(userWithdrawal);
        //리턴 필요 x
    }

    public void logout(String uuId){
        //리프레시토큰을 만료시키기
        redisService.expireValues(uuId);
    }

}