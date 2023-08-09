package com.mohey.authservice.dto;

import com.mohey.authservice.domain.User;
import lombok.*;

import javax.validation.constraints.NotEmpty;

@AllArgsConstructor
@ToString
@Setter
@Getter
public  class JoinRespDto {

    @NotEmpty
    private String memberUuid;

}