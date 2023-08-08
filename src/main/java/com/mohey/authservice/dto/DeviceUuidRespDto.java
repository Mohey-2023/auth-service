package com.mohey.authservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class DeviceUuidRespDto {

    private String memberUuid;
    private String deviceUuid;
    private String deviceToken;

}
