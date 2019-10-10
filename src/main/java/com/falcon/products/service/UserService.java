package com.falcon.products.service;

import com.falcon.products.domain.User;

public interface UserService {

    User getUserByLogin(String login);
}
