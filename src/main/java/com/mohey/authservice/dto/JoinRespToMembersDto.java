package com.mohey.authservice.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@Data
public class JoinRespToMembersDto {

    @NotEmpty
    @Size(min = 4, max = 100)
    private String memberUuid;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @NotEmpty
    private LocalDateTime birthDate;

    @NotEmpty
    private String gender;

    @NotEmpty
    private String deviceUuid;

    @NotEmpty
    private String deviceToken;

    @NotEmpty
    @Size(max=100)
    private String selfIntroduction;

    @NotEmpty
    private String profileUrl;

    @NotEmpty
    private List<String> interests;

    // 영어, 한글, 1~20
    //@NotEmpty
    @Pattern(regexp = "^[a-zA-Z가-힣]{1,10}$", message = "한글/영문 1~20자 이내로 작성해주세요")
    private String nickname;

    @Builder
    public JoinRespToMembersDto(String memberUuid, LocalDateTime birthDate, String gender, String deviceUuid, String deviceToken, String selfIntroduction, String profileUrl, List<String> interests, String nickname) {
        this.memberUuid = memberUuid;
        this.birthDate = birthDate;
        this.gender = gender;
        this.deviceUuid = deviceUuid;
        this.deviceToken = deviceToken;
        this.selfIntroduction = selfIntroduction;
        this.profileUrl = profileUrl;
        this.interests = interests;
        this.nickname = nickname;
    }
}