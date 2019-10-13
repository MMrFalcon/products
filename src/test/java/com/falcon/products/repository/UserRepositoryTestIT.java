package com.falcon.products.repository;

import com.falcon.products.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import javax.persistence.EntityManager;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryTestIT {

    private final String USER_LOGIN = "Jack";
    private final String USER_PASSWORD = "secret";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityManager entityManager;

    @BeforeEach
    void setUp() {
        entityManager.persist(User.builder().login(USER_LOGIN).password(USER_PASSWORD).build());
        entityManager.flush();
    }

    @Test
    void findUserByLogin() {
        Optional<User> savedUser;
        savedUser = userRepository.findUserByLogin(USER_LOGIN);

        assertTrue(savedUser.isPresent());
        assertEquals(USER_LOGIN, savedUser.get().getLogin());
        assertEquals(USER_PASSWORD, savedUser.get().getPassword());
        assertNotNull(savedUser.get().getId());
    }
}