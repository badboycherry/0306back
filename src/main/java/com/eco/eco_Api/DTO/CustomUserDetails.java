package com.eco.eco_Api.DTO;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.eco.eco_Api.entity.UserEntity;

public class CustomUserDetails implements UserDetails {

    private final UserEntity userEntity; 

    // 생성자에서 Optional 대신 UserEntity 직접 받음
    public CustomUserDetails(UserEntity user) {
        this.userEntity = user;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        // userEntity가 null이 아닌 경우에만 권한 추가
        if (userEntity != null) {
            authorities.add(() -> userEntity.getRole());
        }
        return authorities;
    }


    @Override
    public String getPassword() {
        return userEntity != null ? userEntity.getPassword() : null;
    }

    @Override
    public String getUsername() {
        return userEntity != null ? userEntity.getUsername() : null;
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