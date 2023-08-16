package com.mohey.authservice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.mohey.authservice.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {

	// select * from user where username = ? 이렇게 동작

	@Query("SELECT u FROM User u LEFT JOIN UserWithdrawal uw ON u.id = uw.userId.id WHERE u.kakaoId = :kakaoId AND (uw.status IS NULL OR uw.status = true) AND u.createdDatetime = (SELECT MAX(u2.createdDatetime) FROM User u2 WHERE u2.kakaoId = :kakaoId)")
	Optional<User> findByKakaoId(String kakaoId);

	// Jpa NameQuery 작동
	//Optional<User> findByUsername(String username);
	//null일 수도 있어서 감싸주는거 null 방어코드 짜줘야함
	User findByMemberUuid(String memberUuid);

	// save - 이미 만들어져 있음. jparepo가 이미 만들어 줌 테스트 필요 없다
}