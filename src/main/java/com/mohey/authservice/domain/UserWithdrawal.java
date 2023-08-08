package com.mohey.authservice.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.type.descriptor.sql.TinyIntTypeDescriptor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.util.Lazy;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

import static javax.persistence.FetchType.LAZY;

@Setter
@NoArgsConstructor // 스프링이 User 객체생성할 때 빈생성자로 new를 하기 때문!!
@Getter
@EntityListeners(AuditingEntityListener.class)//날짜 자동 등록 위해
@Table(name = "member_withdrawal_tb") //테이블 이름
@Entity //JPA 엔티티
public class UserWithdrawal { // extends 시간설정 (상속)

    //jpa 설정하기
    @Id//기본 키
    //자동 생성되는 값으로 기본 키를 설정.
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "member_auth_tb_id")
    //카카오 아이디 아님 !! uuid도 아님 !! 유저랑 조인할 때 쓸 유저의 자동증가 id..
    //@Column(unique = true, nullable = false, length = 20)
    private User userId;

    //not null
    @Column(nullable = false)
    private Boolean status;

    //@NotEmpty
    @LastModifiedDate // Insert, Update 시 날짜 자동 들어감
    @Column(nullable = false)
    private LocalDateTime createdDatetime;

    @Builder
    public UserWithdrawal(Long id, User userId, Boolean status, LocalDateTime createdDatetime) {
        this.id = id;
        this.userId = userId;
        this.status = status;
        this.createdDatetime = createdDatetime;
    }
}