package com.mohey.authservice.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.mohey.authservice.domain.User;
import com.mohey.authservice.domain.UserEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.Column;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Setter
@Getter
@ToString
public class JoinReqDto {

    @NotEmpty
    private String accessToken;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime birthDate;

    @NotEmpty
    private String gender;

    @NotEmpty
    private String deviceToken;

    @NotEmpty
    @Size(max=100)
    private String selfIntroduction;

    @NotEmpty
    private String profileUrl;

    @NotEmpty
    private List<String> interests;

    //@NotEmpty
    @Pattern(regexp = "^[a-zA-Z가-힣]{1,10}$", message = "한글/영문 1~20자 이내로 작성해주세요")
    private String nickname;


}