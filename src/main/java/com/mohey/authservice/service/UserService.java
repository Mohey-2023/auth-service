package com.mohey.authservice.service;

import com.mohey.authservice.dto.DeleteReqDto;
import com.mohey.authservice.dto.DeviceUuidDto;
import com.mohey.authservice.dto.JoinReqDto;
import com.mohey.authservice.dto.JoinRespDto;

import java.rmi.ServerException;

public interface UserService {
    public JoinRespDto join(JoinReqDto joinReqDto) throws ServerException;

    public void withDraw(DeleteReqDto deleteReqDto);

    public void logout(String memberUuid);

    Boolean verifyRefreshToken(String refreshToken);

    void verifyDeviceUuid(String deviceUuid,String deviceToken, String memberUuid);
}
