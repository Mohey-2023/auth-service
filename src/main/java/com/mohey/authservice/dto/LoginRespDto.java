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

    private String memberUuid;

    public LoginRespDto(String memberUuid) {

        this.memberUuid = memberUuid;

    }
    @Builder
    public LoginRespDto(User user) {
        this.memberUuid = user.getMemberUuid();

    }
}