package com.mohey.authservice.dto;


import com.mohey.authservice.domain.User;
import com.mohey.authservice.util.CustomDateUtil;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginRespDto {
    private Long id;
    private String username;
    private String createdAt;
    private String uuId;
    //private String profilePic;
    public LoginRespDto(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.uuId = user.getUuId();
        this.createdAt = CustomDateUtil.toStringFormat(user.getCreatedAt());
        //this.profilePic = user.getProfilePic();
    }
}

