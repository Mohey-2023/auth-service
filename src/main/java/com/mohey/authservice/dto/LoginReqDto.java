package com.mohey.authservice.dto;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Setter
@Getter
public class LoginReqDto {

    @NotEmpty
    private String accessToken;

    @NotEmpty
    private String deviceUuid;

    @NotEmpty
    private String deviceToken;

}