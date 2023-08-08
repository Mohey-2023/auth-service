package com.mohey.authservice.config.auth;

import java.util.ArrayList;
import java.util.Collection;

import com.mohey.authservice.dto.DeviceUuidRespDto;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.mohey.authservice.domain.User;

import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public class LoginUser implements UserDetails {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private final User user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(() -> "ROLE_" + user.getRole());
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    //바꾸면 터지는거
    //오...
    @Override
    public String getUsername() {
        return user.getKakaoId();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}