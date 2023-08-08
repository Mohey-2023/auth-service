package com.mohey.authservice.dto;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.mohey.authservice.domain.User;
import com.mohey.authservice.util.CustomDateUtil;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;


@Setter
@Getter
public class LoginRespDto {
    private Long id;
    private String kakaoId;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime createdDateTime;
    private String memberUuid;

//    private String deviceUuid;

//    private String deviceToken;
    //private String deviceUuid;
    //private String deviceToken;

    public LoginRespDto(Long id, String kakaoId, LocalDateTime createdDateTime, String memberUuid) {
        this.id = id;
        this.kakaoId = kakaoId;
        this.createdDateTime = createdDateTime;
        this.memberUuid = memberUuid;
        //this.deviceUuid = deviceUuid;
        //this.deviceToken = deviceToken;
    }
    @Builder
    public LoginRespDto(User user) {
        this.id = user.getId();
        this.kakaoId = user.getKakaoId();
        this.memberUuid = user.getMemberUuid();
        this.createdDateTime = user.getCreatedDatetime();

    }
}

