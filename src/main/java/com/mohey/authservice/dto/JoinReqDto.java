package com.mohey.authservice.dto;

import com.mohey.authservice.domain.User;
import com.mohey.authservice.domain.UserEnum;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.UUID;

@Setter
@Getter
public class JoinReqDto {

    //왜 인서트에서 유저네임이 null로가버릴까.....
    //이거 카카오아이디임
    //길이 4~20
    @NotEmpty
    @Size(min = 4, max = 100)
    private String username;

    @NotEmpty
    @Size(min = 4, max = 100)
    private String uuId;
    UUID uuid= UUID.randomUUID();

    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private String password = passwordEncoder.encode("");

    public User toEntity() {
        return User.builder()
                .uuId(uuid.toString())
                .username(username)
                .role(UserEnum.MEMBER)
                .password(password)
                .createdAt(LocalDateTime.now())
                .build();
    }

}
