package com.mohey.authservice.domain;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.*;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Getter;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@NoArgsConstructor // 스프링이 User 객체생성할 때 빈생성자로 new를 하기 때문!!
@Getter
@EntityListeners(AuditingEntityListener.class)//날짜 자동 등록 위해
@Table(name = "member_auth_tb") //테이블 이름
@Entity //JPA 엔티티
public class User { // extends 시간설정

    //연관관계는 이렇게 걸어야함
    //유저 삭제는 유저를 알아야하고 유저는 유저 삭제 정보를 알 필요 x
    //관계 x...필요할 때 조인
//    @OneToOne(mappedBy = "UserWithdrawal_id")
//    private UserWithdrawal userWithdrawal;

    //jpa 설정하기

    //userId, deviceId를 가진 테이블 auth 추가
    //회원가입시 user테이블 거랑 deviceId(클라이언트가 던짐)
    //카카오아이디만 처리해서넣으면 ok

    @Id//기본 키
    //자동 생성되는 값으로 기본 키를 설정.
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //카카오아이디...
    @Column(unique = true, nullable = false, length = 20)
    private String kakaoId;

    //Column(nullable = false, length = 30)
    //private String kakaoId;

    //@OneToOne(mappedBy = "UserWithdrawal_uuId")
    //cascade?
    @Column(unique = true, nullable = false, length = 100)
    //@JoinColumn(name = "UserWithdrawal_uuId")
    private String memberUuid;


//    @Column(nullable = false, length = 20)
//    private String fullname;

    //시큐리티가 강제로
    @Column(nullable = false, length = 60) // 패스워드 인코딩(BCrypt) 카카오아이디 로그인이어도 패스워드는 필요함
    private String password;
    // Enum 타입을 문자열로 매핑

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserEnum role; // ADMIN, CUSTOMER


    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @LastModifiedDate // Insert, Update 시 날짜 자동 들어감
    @Column(nullable = false)
    private LocalDateTime createdDatetime;

    @Builder
    public User(String kakaoId, String memberUuid, String password, UserEnum role, LocalDateTime createdDatetime) {
        this.kakaoId = kakaoId;
        this.memberUuid = memberUuid;
        this.password = password;
        this.role = role;
        this.createdDatetime = createdDatetime;
    }
}