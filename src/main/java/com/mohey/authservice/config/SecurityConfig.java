package com.mohey.authservice.config;

import com.mohey.authservice.config.jwt.JwtAuthenticationFilter;
import com.mohey.authservice.config.jwt.JwtAuthorizationFilter;
import com.mohey.authservice.domain.UserEnum;
import com.mohey.authservice.util.CustomResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;


@Configuration
public class SecurityConfig {
    private final Logger log = LoggerFactory.getLogger(getClass());

    private final CustomResponseUtil customResponseUtil;

    @Autowired
    public SecurityConfig(CustomResponseUtil customResponseUtil) {
        this.customResponseUtil = customResponseUtil;
    }

    // JWT 필터 등록이 필요함 정해진 형식이 있는거라서 외워야 함
    public class CustomSecurityFilterManager extends AbstractHttpConfigurer<CustomSecurityFilterManager, HttpSecurity> {
        @Override
        public void configure(HttpSecurity builder) throws Exception { //여기서 필터 등록
            AuthenticationManager authenticationManager = builder.getSharedObject(AuthenticationManager.class);//강제 세션로그인 하기 위해서 필요
            builder.addFilter(new JwtAuthenticationFilter(authenticationManager, customResponseUtil)); //필터 등록
            builder.addFilter(new JwtAuthorizationFilter(authenticationManager));
            super.configure(builder);
        }
    }
    @Bean // Ioc 컨테이너에 BCryptPasswordEncoder() 객체가 등록됨.
    public BCryptPasswordEncoder passwordEncoder() {
        log.debug("디버그 : BCryptPasswordEncoder 빈 등록됨"); //이렇게 디버그로 잘 실행이 되는지 화깅ㄴ하자!
        return new BCryptPasswordEncoder();
    }

    // JWT 서버를 만들 예정!! Session 사용안함.
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        log.debug("디버그 : filterChain 빈 등록됨");
        http.headers().frameOptions().sameOrigin(); // iframe 허용안함.
        http.csrf().disable(); // enable이면 post맨 작동안함
        http.cors().configurationSource(configurationSource());

        // jSessionId를 서버쪽에서 관리안하겠다는 뜻!!
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        // react, 앱으로 요청할 예정
        http.formLogin().disable();
        // httpBasic은 브라우저가 팝업창을 이용해서 사용자 인증을 진행한다.
        http.httpBasic().disable();

//        // 필터 적용
        http.apply(new CustomSecurityFilterManager());
//
        // 인증 실패 즉 권한이 없는 사람이 권한 필요한 페이지로 들어갔을 때 그 예외의 헨들링을 시큐리티에서는 원래 자동으로 해주는데
        // 그걸 내가 가져와서 설정한다.
        //필터 통과 후 컨트롤러로 갔는데 주소가 api/s이면 security session에 값이 있는지 확인한다 없으면 아래 예외 발생
        http.exceptionHandling().authenticationEntryPoint((request, response, authException) -> {

            //아래처럼 403 에러가 뜰 때는 error라는 글자를 출력하게 한다
            //이렇게 하면 홈페이지에서 들어갈떄나, 백엔드에서나, postman에서나 403에러 페이지의 body에 error문구만 출력됨 나머진 출략 x
            //이렇게 에러를 통제해야 한느것!!!
//        	ObjectMapper om = new ObjectMapper();
//        	ResponseDto<?> responseDto = new ResponseDto<>(-1, "권한없음", null);//에러 반환 Dto클래스만들고 객체생성
//        	//json으로 변경후 반환
//        	System.out.println("rwr"+ responseDto);
//        	String responseBody = om.writeValueAsString(responseDto);
//        	System.out.println("body"+ responseBody);
//        	response.setContentType("application/json; charser=utf-8");
//        	response.setStatus(401); 중요! 로그인이 필여한 서비스는 401 권한이 없어 금지된건 403 근데 스프링이 api/s/ 를 403을 던져주니 우린 강제로 401주기
//        	//response.getWriter().println("error");
//        	response.getWriter().println(responseBody);//error말고 위에 json으로 반환하기

            //위의 문구들은 여기에 두면 가독성이 떨어지니까 CustomResponseUtil 이라는 클래스 만들고 fail 이라는 메서드에 넣어서 아래처럼 작업하기
            // HttpStatus.UNAUTHORIZED 이게 401에러
            CustomResponseUtil.fail(response, "로그인을 진행해 주세요", HttpStatus.UNAUTHORIZED);
        });
//
//        // 권한 실패 필터 통과 후 컨트롤러로 갔는데 주소가 api/admin이면 security session에 있는 값에 ROLE_ ~~ 인 값을 가진애가 있는지 검증한다
        //없다면 아래 꺼 실행
        http.exceptionHandling().accessDeniedHandler((request, response, e) -> {
            CustomResponseUtil.fail(response, "권한이 없습니다", HttpStatus.FORBIDDEN);
        });

        // https://docs.spring.io/spring-security/reference/servlet/authorization/authorize-http-requests.html
        //공식문서 ROLE_ 안써도 된다 이제 알아서 prefix로 붙여줌
        http.authorizeRequests()
                .antMatchers("/api/s/**").authenticated() //포스트맨으로 http://localhost:8081/api/admin/dsd 이런식으로 테스트 해보면 403 에러 확인
                .antMatchers("/api/admin/**").hasRole("" + UserEnum.ADMIN) // 최근 공식문서에서는 ROLE_ 안붙여도 됨
                //이 주소로 갈때 role이 UserEnum의 ADMIN일때만 가능하다.
                .anyRequest().permitAll(); //나머지 요청은 허용

        return http.build();
    }

    public CorsConfigurationSource configurationSource() {
        log.debug("디버그 : configurationSource cors 설정이 SecurityFilterChain에 등록됨");
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*"); // GET, POST, PUT, DELETE (Javascript 요청 허용)
        configuration.addAllowedOriginPattern("*"); // 모든 IP 주소 허용 (프론트 앤드 IP만 허용 react 나중에 바꺼야함)
        configuration.setAllowCredentials(true); // 클라이언트에서 쿠키 요청 허용
        configuration.addExposedHeader("Authorization"); // 옛날에는 디폴트 였다. 지금은 아닙니다.
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);//모든 주소 요청에 위 설정을 적용하겠다.
        return source;
    }

}