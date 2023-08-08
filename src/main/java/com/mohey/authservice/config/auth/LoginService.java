package com.mohey.authservice.config.auth;

import com.mohey.authservice.domain.User;
import com.mohey.authservice.repository.UserRepository;
import com.mohey.authservice.service.KaKaoLoginService;
import com.mohey.authservice.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class LoginService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    KaKaoLoginService kakaoLoginService;

    @Autowired
    RedisService redisService;
    // 시큐리티로 로그인이 될때, 시큐리티가 loadUserByUsername() 실행해서 username을 체크!!
    // db체크는 직접 해야함(메서드 만들어야 함)
    // 없으면 오류 -> 시큐리티 하고 있을 때는 제어권이 시큐리티에 있으니 시큐리니 에러를 던져야 함
    // 있으면 정상적으로 시큐리티 컨텍스트 내부 세션에 로그인된 세션이 만들어진다.
    @Override
    //username만 들어옴
    public UserDetails loadUserByUsername(String accessToken) throws UsernameNotFoundException {
        String kakaoId = kakaoLoginService.createKakaoUser(accessToken);
        System.out.println("kakaoId: " + kakaoId);
        System.out.println("loginService");
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        System.out.printf("password" + passwordEncoder.encode(""));
        //더미데이터들어가있음
        User userPS = userRepository.findByKakaoId(kakaoId).orElseThrow(
                () -> new InternalAuthenticationServiceException("인증 실패")); // 나중에 테스트할 때 설명해드림.
//            Authentication 에 있는 거랑 userPs username pass와 자동비교




        //System.out.println("uuId: " + userPS.getMemberUuid());
        System.out.println("userPds: " + userPS.getMemberUuid());
        System.out.println("userPdspassword: " + userPS.getPassword());
        return new LoginUser(userPS);
    }

}