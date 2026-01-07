package com.kwewk.course_project.service;

import com.kwewk.course_project.dto.UserDTO;
import com.kwewk.course_project.exception.NotFoundException;
import com.kwewk.course_project.model.User;
import com.kwewk.course_project.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + id));
        return convertToDTO(user);
    }

    @Transactional(readOnly = true)
    public UserDTO getUserByName(String name) {
        User user = userRepository.findByName(name)
                .orElseThrow(() -> new NotFoundException("User not found with name: " + name));
        return convertToDTO(user);
    }

    @Transactional(readOnly = true)
    public boolean userExists(Long id) {
        return userRepository.existsById(id);
    }

    private UserDTO convertToDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .isRegistered(user.getIsRegistered())
                .build();
    }
}