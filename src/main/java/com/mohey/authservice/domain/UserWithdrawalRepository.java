package com.mohey.authservice.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface UserWithdrawalRepository extends JpaRepository<UserWithdrawal, Long> {

    // select * from user where username = ? 이렇게 동작
    //Optional<User> findByKakaoId(String username); // Jpa NameQuery 작동
    Optional<UserWithdrawal> findByUserId(String userId);
    // save - 이미 만들어져 있음. jparepo가 이미 만들어 줌 테스트 필요 없다
}