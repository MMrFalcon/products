package com.falcon.products.repository;

import com.falcon.products.domain.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findUserByLogin(String login);
}
