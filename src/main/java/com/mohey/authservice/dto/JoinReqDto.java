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
    //왜 인서트에서 유저네임이 null로가버릴까.....
    //이거 카카오아이디임
    //길이 4~20
    @NotEmpty
    @Size(min = 4, max = 100)
    private String kakaoId;

    @NotEmpty
    @Size(min = 4, max = 100)
    private String memberUuid;
    UUID uuid= UUID.randomUUID();

    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private String password = passwordEncoder.encode("");

    @NotEmpty
    @Size(min= 4, max = 100)
    private String deviceUuid;
    UUID dUuid = UUID.randomUUID();

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

    // 이메일 형식 message여기에 오류 메세지가 들어가서 출력 됨 email이면 email: 이메일 형식으로 작성해 주세요
    //@NotEmpty
    //@Pattern(regexp = "^[a-zA-Z0-9]{2,10}@[a-zA-Z0-9]{2,6}\\.[a-zA-Z]{2,3}$", message = "이메일 형식으로 작성해주세요")
    //private String email;

    // 영어, 한글, 1~20
    //@NotEmpty
    @Pattern(regexp = "^[a-zA-Z가-힣]{1,10}$", message = "한글/영문 1~20자 이내로 작성해주세요")
    private String nickname;

    public User toEntity() {
        return User.builder()
                .memberUuid(uuid.toString())
                .kakaoId(kakaoId)
                .role(UserEnum.MEMBER)
                .password(password)
                .createdDatetime(LocalDateTime.now())
                .build();
    }

    public JoinRespToMembersDto toMemberEntity(){
        return JoinRespToMembersDto.builder()
                .memberUuid(uuid.toString())
                .gender(gender)
                .birthDate(birthDate)
                .deviceToken(deviceToken)
                .deviceUuid(dUuid.toString())
                .nickname(nickname)
                .selfIntroduction(selfIntroduction)
                .interests(interests)
                .profileUrl(profileUrl)
                .build();
    }

}
