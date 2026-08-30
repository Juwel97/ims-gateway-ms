package com.ims.common;

import com.ims.common.controller.AuthController;
import com.ims.common.dto.ChangePasswordRequest;
import com.ims.common.model.User;
import com.ims.common.model.UserRole;
import com.ims.common.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
class UserControllerIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthController authController;

    @Test
    void shouldPersistUserInDatabase() {
        User user = new User();
        user.setEmail("testuser@example.com");
        user.setPassword(passwordEncoder.encode("secret123"));
        user.setFirstName("Test");
        user.setMiddleName("Middle");
        user.setLastName("User");
        user.setPhoneNumber("1234567890");
        user.setAddress("Test address");
        user.setCreatedAt(LocalDateTime.now());
        user.setCreatedBy("admin");
        user.setLoginAttempt(0);
        user.setRole(UserRole.STUDENT);

        User saved = userRepository.save(user);

        assertNotNull(saved.getId());
        assertEquals("testuser@example.com", saved.getEmail());
        assertEquals(UserRole.STUDENT, saved.getRole());
        assertEquals(0, saved.getLoginAttempt());
    }

}
