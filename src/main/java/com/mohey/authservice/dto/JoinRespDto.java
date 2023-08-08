package com.mohey.authservice.dto;

import com.mohey.authservice.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Setter
@Getter
public  class JoinRespDto {
    //private Long id;
    private String memberUuid;
    //private String kakaoId;

    @Builder
    public JoinRespDto(User user) {
        //this.id = user.getId();
        this.memberUuid = user.getMemberUuid();
        //this.kakaoId = user.getKakaoId();

    }
}