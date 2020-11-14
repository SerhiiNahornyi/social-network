package com.kpi.project.service;

import com.kpi.project.model.User;
import com.kpi.project.model.userRole.Role;
import com.kpi.project.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    private User user;

    @InjectMocks
    private UserService testingInstance;

    @BeforeEach
    public void setUp() {
        user = new User(1L, "mail@mail.com", "username", "password", Collections.singletonList(Role.ADMIN) );
    }

    @Test
    public void loadUserByUsernameShouldReturnUserFoundByEmailOrUsername() {
        // given
        given(userRepository.loadByEmailOrUsername("login")).willReturn(user);

        // when
        final User actualUser = testingInstance.loadUserByUsername("login");

        // then
        verify(userRepository).loadByEmailOrUsername("login");
        assertThat(actualUser).isEqualTo(user);
    }
}
