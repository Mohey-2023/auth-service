package com.mohey.authservice.config.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.mohey.authservice.config.auth.LoginUser;
import com.mohey.authservice.dto.LoginReqDto;
import com.mohey.authservice.dto.LoginRespDto;
import com.mohey.authservice.service.KaKaoLoginService;
import com.mohey.authservice.util.CustomResponseUtil;
import com.fasterxml.jackson.databind.ObjectMapper;



//로그인 인증 필터임 securityconfig에서 따로 등록이 필요함
//UsernamePasswordAuthenticationFilter 시큐리티 필터임/api/login 주소 에서만 동작

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final Logger log = LoggerFactory.getLogger(getClass());
    private AuthenticationManager authenticationManager;

    private final CustomResponseUtil customResponseUtil;
    private KaKaoLoginService kakaoLoginService;


//    @Autowired
//    UserRepository userRepository;
// bean스캔불가능....


    //생성자
    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, CustomResponseUtil customResponseUtil) {
        super(authenticationManager);
        this.customResponseUtil = customResponseUtil;
        setFilterProcessesUrl("/members/login"); //주소 변경 기본은  /login
        this.authenticationManager = authenticationManager;
    }

    // Post : /api/login 이러면 동작
    //login시 이 필터를 제일 먼저 거친다
    // 여기서는 로그인 데이터를 받아 강제 로그인(세션) 진행 후
    //successfulAuthentication 메서드에서 jwt 발급받아서 전달해줌
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        log.debug("디버그 : attemptAuthentication 호출됨");
        try {
            ObjectMapper om = new ObjectMapper();
            //request에 있는 json데이터를 LoginReqDto 타입으로 꺼내기
            //username과 password가 존재함

            System.out.println("eeeeewwwwwwwwwwwww");
            LoginReqDto loginReqDto = om.readValue(request.getInputStream(), LoginReqDto.class);
            String username = loginReqDto.getAccessToken();

            //로그인시 공백으로라도 넣어줘야함
            String password = "";

            System.out.println("eeeeewwwwwwwwwwwewewqthfghfww");

            //KaKaoLoginService kaka = new KakaoLoginServiceimpl();


            // 강제 로그인 토큰만들기: username과 passwor가 담긴 토큰을 만듬
            // jwt토큰이 아님  인증을 위한 토큰

            // authenticationManager.authenticate 메서드는
            // UserDetailsService의 loadUserByUsername 호출 -> 시큐리티가 db확인 후 세션생성함 db에 없다면 예외 던짐
            //이 예외는 @RestControllerAdvice 여기서 처리하지 못하기 때문에 이 필터 내의
            //unsuccessfulAuthentication 애가 예외를 처리한다. userDetailsService참고
            //
            // JWT를 쓴다 하더라도, 컨트롤러 진입을 하면 시큐리티의 권한체크, 인증체크의 도움을 받을 수 있게 세션을 만든다.
            // 이 세션의 유효기간은 request하고, response하면 끝!! 이 세션은 임시일 뿐
            //즉 다음 요청때 이 세션은 의미 없음 JSESSIONID를 주고받지 않기 때문
            //강제로그인 하기
            //성공시 authentication에 LoginUser 객체 정보가 담김


            //소셜 로그인 시 password가 없기 때문에 아래 방법으로 진행 이 후 authenticate 실행 말고 return 하면됨
            //일반 로그인과 같이 사용하기 때문에 조건 줘서 나눠서 처리해줘야함
            //소셜 로그인 id에 따라서 실패 경우도 생각 해야함.. 실패는 사실상 회원가입을 의미한다.
            //throw new InternalAuthenticationServiceException(e.getMessage()); 예외 발생시켜
            //unsuccessfulAuthentication 처리 적절히 응답후 회원가입 페이지로 리다이렉트 시키면 된다.
            //userdetailsevice호출해서 db에서 값 가져와야한다. 가져와서 id값 비교 하면 됨
            //그리고 가져온것들을 user에 넣어 role같은것도..??

            //User userPS = userRepository.findByUsername(loginReqDto.getKakaoId());

            //User userPS = userRepository.findByKakaoId(loginReqDto.getKakaoId()).orElseThrow(
            //            () -> new InternalAuthenticationServiceException("인증 실패"));
            System.out.println("eaaaaaaaaaaaaaaae");
            //    User userPs = new User();
            System.out.println("username: "+username);
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    username, password);
            //키값 맞춰줘야함 LoginService 참고
            //여기못감 !!
            //카카오...
            System.out.println("username: "+username);
            Authentication authentication = authenticationManager.authenticate(authenticationToken);

            System.out.println("eaaaaaaaaaaaaaaae232323");

            // UserDetails userDetail = new LoginUser(userPs);
//            Authentication authentication = new UsernamePasswordAuthenticationToken(userDetail, null,
//                    userDetail.getAuthorities());


            // SecurityContextHolder.getContext().setAuthentication(authentication); return할때 이 작업 수행해줌



//            authenticationManager.authenticate() 메서드는 주어진 authenticationToken을 사용하여 사용자를 인증하고,
//            인증이 성공한 경우 Authentication 객체를 반환
//            authenticationManager.authenticate() 메서드 호출은 스프링 시큐리티에서 인증 프로세스를 수행하고,
//            인증된 사용자를 나타내는 세션을 생성

            //애가 return이 되면 successfulAuthentication 이 작동함

            //컨텍스트 홀더ㅔㅇ 넣어버림
            return authentication;
        } catch (Exception e) {
            // unsuccessfulAuthentication 호출함
            System.out.println("error");
            throw new InternalAuthenticationServiceException(e.getMessage());
        }
    }

    // 로그인 실패
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) throws IOException, ServletException {
        customResponseUtil.fail(response, "로그인실패", HttpStatus.UNAUTHORIZED);
        //회원가입 로직
    }

    // return authentication 잘 작동하면 successfulAuthentication 메서드 호출됩니다.
    // 즉 로그인 성공 시 내용 들어가면 됨
    // JWT토큰을 만들고 헤더에 넣어 리턴하는게 끝임
    //JWT토큰 만들 떄 LOGINUSER정보를 같이 넣는다!~
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        log.debug("디버그 : successfulAuthentication 호출됨");
        LoginUser loginUser = (LoginUser) authResult.getPrincipal(); //로그인 유저 정보
        String accessToken = JwtProcess.createAccessToken(loginUser); // 이 로그인 유저로 jwt 액세스 토큰 만들기
        String refreshToken = JwtProcess.createRefreshToken(loginUser);//리프레시 토큰도 만들어줘야함
        //String memberUuid = loginUser.getUser().getMemberuuid();
        // response.addHeader("UUID", memberUuid);
        response.addHeader(JwtVO.HEADER, accessToken); // 헤더에 토큰 담아
        response.addHeader("RefreshToken", refreshToken);

        //redisService 의존성주입 못해서. . . 다른곳에서 해야할듯
        //db에 refresh 토큰 저장해야함
//        redisService.setValues(loginUser.getUsername(),refreshToken);
        //System.out.println("outOfRedis");
//        redisService.getValues(loginUser.getUsername());

        LoginRespDto loginRespDto = new LoginRespDto(loginUser.getUser());
        customResponseUtil.success(response, loginRespDto);
    }




}