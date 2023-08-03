package com.mohey.authservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    public String getValues(String key){
        //opsForValue : Strings를 쉽게 Serialize / Deserialize 해주는 Interface
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        //System.out.println("OutOfRedis22");
        //System.out.println(values.get(key));
        return values.get(key);
    }

    public void setValues(String key, String value){
        redisTemplate.opsForValue().set(key,value);
        //어차피 리프레시 토큰만 담으니까
        //유효기간 설정까지
        redisTemplate.expire(key,1,TimeUnit.DAYS);
    }

    //해당 키값에 맞는 리프레시토큰(밸류) 찾아서 유효시간 만료시키자
    public void expireValues(String key){
        redisTemplate.expire(key,0, TimeUnit.SECONDS);
    }

    public void setSets(String key,String... values){
        redisTemplate.opsForSet().add(key,values);
    }

    public Set getSets(String key){
        return redisTemplate.opsForSet().members(key);
    }


    //이건...데이터 넣는거같음 uuid랑 refreshToken넣어주자!!
//    public ResponseEntity<?> addRedisKey() {
//        //redis에 유효기간도 담을 수 있다고함
//        //ValueOperations<String, String> vop = redisTemplate.opsForValue();
//        redisTemplate.opsForValue().set("asdfasdf", "refreshToken~");
//        redisTemplate.expire("asdfasdf", 60*60*24, TimeUnit.SECONDS);
//        redisTemplate.opsForValue().set("red", "apple");
//        redisTemplate.opsForValue().set("green", "watermelon");
//        return new ResponseEntity<>(HttpStatus.CREATED);
//    }
//
//    //검색
//    public ResponseEntity<?> getRedisKey(@PathVariable String key) {
//        ValueOperations<String, String> vop = redisTemplate.opsForValue();
//        String value = vop.get(key);
//        return new ResponseEntity<>(value, HttpStatus.OK);
//    }
}