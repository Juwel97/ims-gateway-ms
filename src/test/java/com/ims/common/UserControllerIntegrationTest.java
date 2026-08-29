package com.ims.common;

import com.ims.common.model.User;
import com.ims.common.model.UserRole;
import com.ims.common.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles("test")
class UserControllerIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

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
        user.setRole(UserRole.STUDENT);

        User saved = userRepository.save(user);

        assertNotNull(saved.getId());
        assertEquals("testuser@example.com", saved.getEmail());
        assertEquals(UserRole.STUDENT, saved.getRole());
    }
}
