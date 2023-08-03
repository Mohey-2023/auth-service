package com.mohey.authservice.config.jwt;
/*
 * SECRET 노출되면 안된다. (클라우드AWS - 환경변수, 파일에 있는 것을 읽어서 써야 한다)
 * 리플래시 토큰 구현 안함
 */
public interface JwtVO {
    public static final String SECRET = "모헤이"; // HS256 (대칭키)
    //jwt토큰을 암호화해서 돌려줘야 함 서버만 알고있으면 되는 키 메타쾽(프론트는 키를 가지고 있을 필요가 없음)
    //노출시키면 안되니 db나 환경변수에서 끌어 옴
    public static final int EXPIRATION_TIME = 1000 * 60 * 60 ; // 일주일 1000이 1초
    //한 시간 / 하루 ?
    public static final String TOKEN_PREFIX = "Bearer "; // 한칸 띄우기 주의하기
    public static final String HEADER = "Authorization"; //헤더이름
}