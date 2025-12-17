package com.kwewk.course_project.service;

import com.kwewk.course_project.security.AuthenticationResponse;
import com.kwewk.course_project.dto.request.RegisterRequest;
import com.kwewk.course_project.exception.BadRequestException;
import com.kwewk.course_project.exception.NotFoundException;
import com.kwewk.course_project.model.User;
import com.kwewk.course_project.repository.UserRepository;
import com.kwewk.course_project.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public AuthenticationResponse register(RegisterRequest request) {
        if (userRepository.existsByName(request.getUsername())) {
            throw new BadRequestException("User with name '" + request.getUsername() + "' already exists");
        }

        String hashedPassword = passwordEncoder.encode(request.getPassword());

        User user = User.builder()
                .name(request.getUsername())
                .password(hashedPassword)
                .isRegistered(true)
                .build();

        user = userRepository.save(user);

        CustomUserDetails userDetails = new CustomUserDetails(user);
        String token = jwtService.generateToken(userDetails, user.getId());

        return AuthenticationResponse.of(token, user.getId(), user.getName());
    }

    @Transactional(readOnly = true)
    public AuthenticationResponse login(String username, String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );

        User user = userRepository.findByName(username)
                .orElseThrow(() -> new NotFoundException("User not found: " + username));

        if (!user.getIsRegistered()) {
            throw new BadRequestException("User is not registered");
        }

        CustomUserDetails userDetails = new CustomUserDetails(user);
        String token = jwtService.generateToken(userDetails, user.getId());

        return AuthenticationResponse.of(token, user.getId(), user.getName());
    }
}