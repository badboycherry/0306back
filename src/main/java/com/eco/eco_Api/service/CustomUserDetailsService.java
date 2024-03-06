package com.eco.eco_Api.service;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.eco.eco_Api.DTO.CustomUserDetails;
import com.eco.eco_Api.entity.UserEntity;
import com.eco.eco_Api.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {

        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // DB에서 조회, 사용자가 없으면 UsernameNotFoundException을 던짐
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        // UserEntity 객체를 사용하여 CustomUserDetails 객체 생성
        return new CustomUserDetails(user);
    }
    
}