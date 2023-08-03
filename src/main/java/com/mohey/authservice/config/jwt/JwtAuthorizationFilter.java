package com.mohey.authservice.config.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.mohey.authservice.config.auth.LoginUser;

/*
 * 모든 주소에서 동작함 (토큰 검증)
 */
//BasicAuthenticationFilter 시큐리티 필터임
//securityconfig에서 따로 등록이 필요함
//인가필터. login에서는 이 필터가 낚아채지 않고 login필터가 낚아챔
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {
    private final Logger log = LoggerFactory.getLogger(getClass());

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    // JWT 토큰 헤더를 추가하지 않아도 해당 필터는 통과는 할 수 있지만, 결국 시큐리티단에서 세션 값 검증에 실패함.
    // 헤더가 없으면 바로  chain.doFilter(request, response); 실행돼서
    //security타고 바로 컨트롤러진입 시도
    //해당 컨트롤러 주소가 api/s 이면 security 체인에 있는
    //http.exceptionHandling().authenticationEntryPoint((request, response, authException) -> {
    //CustomResponseUtil.fail(response, "로그인을 진행해 주세요", HttpStatus.UNAUTHORIZED);
    //이거 발동해서 exception 발생 시킨다.

    //api/s/* 이런식의 주소가 들어오면 메서드 실행
    //헤더에 JWT 있는지 확인 및 검증함
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        if (isHeaderVerify(request, response)) {
            // 토큰이 존재함
            log.debug("디버그 : 토큰이 존재함");

            String token = request.getHeader(JwtVO.HEADER).replace(JwtVO.TOKEN_PREFIX, "");
            LoginUser loginUser = JwtProcess.verifyAccessToken(token);//검증
            //검증하면 loginUser에 id랑 롤이 들어감 유저네임이랑 패스워드는 비어있음
            log.debug("디버그 : 토큰이 검증이 완료됨");
            //System.out.println("액세스 토큰");
            System.out.println(token);
            // 임시 세션 (UserDetails 타입 or username 둘 중 하나를 넣을 수 있음
            //하지만 username은 비어있기 때문에 못씀 , role만 잘 들어있으면 된다
            //어차피 인증은  인증필터에서 함) 토큰을 강제로 만들기
            //loginDetailsService를 호출하는게 아니라 강제로 Authentication객체(세션) 만듬
            Authentication authentication = new UsernamePasswordAuthenticationToken(loginUser, null,
                    loginUser.getAuthorities()); // id, role 만 존재 ,비밀번호는 null 왜냐하면 JwtProcess.verify(token)
            //에서 jwt에 담긴 것 중 id랑 role만 넣었으니까! (해당 메서드 참조)
            //강제 로그인 하기
            //SecurityContextHolder에 강제로 만든 Authentication 객체 집어넣기
            //세션 사용안한다고 securityConfig에 설정 해놧기 때문에 처음 로그인시 시큐리티 세션 메모리에 저장되어 있던 authentication 정보는 사라져 있기에
            //이렇게 강제로 다시 시큐리티 세션에 넣어 줘야 한다!
            //이렇게 넣어놓고 컨트롤러에서 필요 시 id랑 role을 컨트롤러에서 꺼내 쓰면 된다.

            //또한 이렇게 response되면 사라지는 이 객체의 loginUser정보 즉 임시정보는
            //오직 권환과 인증을 위해 사용한다.
            // 필터가 다 끝난 후 DispatcherServlet에서 컨틀로러로 배정 해줄 떄
            //api/s 이면 시큐리티가 알아서 세션에 authentication 객체가 있는지 확인 후
            //있다면 그대로 컨트롤러 진행 없다면 ㅏㅃ꾸
            // api/admin이면 시큐리티가 authentication객체의 LoginUser객체의 role이
            //Customer인지 어드민인지 확인 후 Customer이면 빠꾸 Admin이면 허용(securityconfig에서 설정햇음)
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.debug("디버그 : 임시 세션이 생성됨");
        }
        chain.doFilter(request, response); //security의 다음 필터로 이동해라
        //필터가 다 끝나면 결국 DispatcherServlet으로 가는 거다.

        //헤더가 없어 if문에 들어가지 못해도 security통과 후 controller갈 때 api/s 나 api/admin이면 securityConfig에서
        //설정한대로 제어 함
    }
    //헤더 검증 메서드
    private boolean isHeaderVerify(HttpServletRequest request, HttpServletResponse response) {
        String header = request.getHeader(JwtVO.HEADER);
        //리프레시 토큰 헤더까지 추가
        if (header == null || !header.startsWith(JwtVO.TOKEN_PREFIX) || !header.startsWith("RefreshToken")) {
            return false;
        } else {
            return true;
        }
    }
}