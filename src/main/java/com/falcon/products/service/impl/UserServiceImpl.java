package com.falcon.products.service.impl;

import com.falcon.products.domain.User;
import com.falcon.products.repository.UserRepository;
import com.falcon.products.service.UserService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User getUserByLogin(String login) {
        Optional<User> optionalUser = userRepository.findUserByLogin(login);
        if (optionalUser.isPresent()) {
            return optionalUser.get();
        } else {
            throw new RuntimeException("User not Found");
        }
    }
}
