package com.kwewk.course_project.security;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthenticationResponse {
    private String token;
    private String tokenType;
    private Long userId;
    private String username;

    public static AuthenticationResponse of(String token, Long userId, String username) {
        return AuthenticationResponse.builder()
                .token(token)
                .tokenType("Bearer")
                .userId(userId)
                .username(username)
                .build();
    }
}