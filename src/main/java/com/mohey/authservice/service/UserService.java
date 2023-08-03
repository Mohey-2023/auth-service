package com.mohey.authservice.service;

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


@RequiredArgsConstructor
@Service
public class UserService {
    private final Logger log = LoggerFactory.getLogger(getClass());
    private final UserRepository userRepository;
    private final UserWithdrawalRepository userWithdrawalRepository;
    private final RedisService redisService;

    // 서비스는 DTO를 요청받고, DTO로 응답한다.
    @Transactional // 트랜잭션이 메서드 시작할 때, 시작되고, 종료될 때 함께 종료
    public JoinRespDto join(JoinReqDto joinReqDto) {

        //수정...해야댐
        User userPS = userRepository.save(joinReqDto.toEntity());

        // 3. dto 응답
        return new JoinRespDto(userPS);
    }

    //로그아웃, 회원탈퇴

    public void withDraw(DeleteReqDto deleteReqDto){

        //회원 탈퇴 상태만 바꿔서 다시 포스트로 쏘기
        User user = userRepository.findByUuId(deleteReqDto.getUuId()).get();
        //status 변경해서 userWithdrawal 만들어주기
        //null이면 잘못됐다고 에러 던지기
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