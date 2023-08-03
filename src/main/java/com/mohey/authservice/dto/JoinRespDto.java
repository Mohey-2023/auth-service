package com.mohey.authservice.dto;

import com.mohey.authservice.domain.User;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Setter
@Getter
public  class JoinRespDto {
    private Long id;
    private String uuId;
    private String username;

    public JoinRespDto(User user) {
        this.id = user.getId();
        this.uuId = user.getUuId();
        this.username = user.getUsername();

    }
}