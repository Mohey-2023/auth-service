package com.mohey.authservice.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Setter
@Getter
public class JoinReqToMembersDto {
    //영문, 숫자는 되고, 길이 최소 2~20자 이내 regularExpression
    @Pattern(regexp = "^[a-zA-Z0-9]{2,20}$", message = "영문/숫자 2~20자 이내로 작성해주세요")
    @NotEmpty // null이거나, 공백일 수 없다. 이 오류에 걸리면 자동으로 username : 비어 있을 수 없습니다. 반환
    private String username;

    //왜 인서트에서 유저네임이 null로가버릴까.....
    //길이 4~20
    @NotEmpty
    @Size(min = 4, max = 100)
    private String kakaoId;

    @NotEmpty
    @Size(min = 4, max = 100)
    private String uuId;

    @NotEmpty
    private LocalDateTime birthDate;

    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private String password = passwordEncoder.encode("");

    @NotEmpty
    private String gender;

    @NotEmpty
    private String deviceToken;

    @NotEmpty
    @Size(max=100)
    private String selfIntroduction;

    @NotEmpty
    private String profilePic;

    @NotEmpty
    private String interests;

    // 이메일 형식 message여기에 오류 메세지가 들어가서 출력 됨 email이면 email: 이메일 형식으로 작성해 주세요
    //@NotEmpty
    //@Pattern(regexp = "^[a-zA-Z0-9]{2,10}@[a-zA-Z0-9]{2,6}\\.[a-zA-Z]{2,3}$", message = "이메일 형식으로 작성해주세요")
    //private String email;

    // 영어, 한글, 1~20
    //@NotEmpty
    @Pattern(regexp = "^[a-zA-Z가-힣]{1,10}$", message = "한글/영문 1~20자 이내로 작성해주세요")
    private String nickname;

    public JoinReqToMembersDto(String uuId, LocalDateTime birthDate, String password, String gender, String deviceToken, String selfIntroduction, String profilePic, String nickname, String interests) {
        this.uuId = uuId;
        this.birthDate = birthDate;
        this.password = password;
        this.gender = gender;
        this.deviceToken = deviceToken;
        this.selfIntroduction = selfIntroduction;
        this.profilePic = profilePic;
        this.nickname = nickname;
        this.interests = interests;
    }
}