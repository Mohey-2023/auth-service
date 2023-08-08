package com.mohey.authservice.repository;

import com.mohey.authservice.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // select * from user where username = ? 이렇게 동작

    Optional<User> findByKakaoId(String kakaoId); // Jpa NameQuery 작동
    //Optional<User> findByUsername(String username);
    //null일 수도 있어서 감싸주는거 null 방어코드 짜줘야함
    User findByMemberUuid(String memberUuid);

    // save - 이미 만들어져 있음. jparepo가 이미 만들어 줌 테스트 필요 없다
}