package com.kpi.project.service;

import com.kpi.project.model.User;
import com.kpi.project.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User loadUserByUsername(String login) throws UsernameNotFoundException {

        return userRepository.loadByEmailOrUsername(login);
    }
}
