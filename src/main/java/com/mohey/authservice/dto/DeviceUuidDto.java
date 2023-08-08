package com.mohey.authservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.validation.constraints.NotEmpty;

@AllArgsConstructor
@Getter
@Setter
public class DeviceUuidDto {

//    private String username;

    private String deviceUuid;

    private String deviceToken;

    public DeviceUuidDto(String deviceUuid) {
        this.deviceUuid = deviceUuid;
    }

    @Override
    public String toString() {
        return "DeviceUuidDto{" +
               // "username='" + username + '\'' +
                ", deviceUuid='" + deviceUuid + '\'' +
                ", deviceToken='" + deviceToken + '\'' +
                '}';
    }
}
