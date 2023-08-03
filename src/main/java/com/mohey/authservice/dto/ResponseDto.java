package com.mohey.authservice.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
@Setter
public class ResponseDto<T> {
    private final Integer code; // 1 성공, -1 실패
    private final String msg;
    private final T data; //data메세지 형태가 다양하니까 제네릭 으로 하자

    @Override
    public String toString() {
        return "ResponseDto [code=" + code + ", msg=" + msg + ", data=" + data + "]";
    }
    public Integer getCode() {
        return code;
    }
    public String getMsg() {
        return msg;
    }
    public T getData() {
        return data;
    }


}