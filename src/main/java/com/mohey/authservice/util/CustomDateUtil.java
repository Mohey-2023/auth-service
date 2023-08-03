package com.mohey.authservice.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CustomDateUtil {

    public static String toStringFormat(LocalDateTime localDateTime) {
        //이렇게 안바꾸면 2023-01-02 이런식으로 리턴 되니 포맷해서 리턴 시키는게 좋ㅇ므
        return localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}