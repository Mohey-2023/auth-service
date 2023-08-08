package com.mohey.authservice.repository;

import com.mohey.authservice.domain.UserDeviceInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserDeviceInfoRepository extends JpaRepository<UserDeviceInfo, Long> {
    //디바이스 아이디로 사용자 찾기 !
    UserDeviceInfo findByDeviceUuid(String deviceUuid);

}
