package com.mohey.authservice.dto;


import java.util.List;

import lombok.Data;
import lombok.Getter;
import lombok.ToString;


@ToString
@Data
public class MemberRespDto<T> {
    private Integer code;
    private String msg;
    Data data;

    @ToString
    @Getter
    static class Data {
        String memberUuid;
    }
}