package com.mohey.authservice.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

import java.time.LocalDateTime;

import static javax.persistence.FetchType.LAZY;

@Getter
@Setter
@Entity
@Table(name="member_auth_device_info_tb")
public class UserDeviceInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //유저의 기기등록정보를 남기기 위한 테이블 !!
    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "member_auth_tb_id")
    private User user;

    @NotEmpty
    @Column(unique = true, nullable = false, length = 100)
    private String deviceUuid;

    //

    @LastModifiedDate // Insert, Update 시 날짜 자동 들어감
    @Column(nullable = false)
    //@NotEmpty
    private LocalDateTime createdDatetime;

    @Builder
    public UserDeviceInfo(User user, String deviceUuid) {
        this.user = user;
        this.deviceUuid = deviceUuid;
    }

    public UserDeviceInfo() {

    }
}
