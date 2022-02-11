/*
 * Copyright 2022 Yuriy Lyskov
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.yuliskov.myauthservice.service;

import org.yuliskov.myauthservice.exception.EmailAlreadyExistsException;
import org.yuliskov.myauthservice.exception.UsernameAlreadyExistsException;
import org.yuliskov.myauthservice.model.Role;
import org.yuliskov.myauthservice.repository.UserRepository;
import org.yuliskov.myauthservice.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserService {

    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private UserRepository userRepository;
    @Autowired private AuthenticationManager authenticationManager;
    @Autowired private JwtTokenProvider tokenProvider;


    public String loginUser(String username, String password) {
       Authentication authentication = authenticationManager
               .authenticate(new UsernamePasswordAuthenticationToken(username, password));

       return tokenProvider.generateToken(authentication);
    }

    public User registerUser(User user, Role role) {
        log.info("registering user {}", user.getUsername());

        if(userRepository.existsByUsername(user.getUsername())) {
            log.warn("username {} already exists.", user.getUsername());

            throw new UsernameAlreadyExistsException(
                    String.format("username %s already exists", user.getUsername()));
        }

        if(userRepository.existsByEmail(user.getEmail())) {
            log.warn("email {} already exists.", user.getEmail());

            throw new EmailAlreadyExistsException(
                    String.format("email %s already exists", user.getEmail()));
        }
        user.setActive(true);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(new HashSet<>() {{
            add(role);
        }});

        return userRepository.save(user);
    }

    public List<User> findAll() {
        log.info("retrieving all users");
        return userRepository.findAll();
    }

    public Optional<User> findByUsername(String username) {
        log.info("retrieving user {}", username);
        return userRepository.findByUsername(username);
    }

    public Optional<User> findById(String id) {
        log.info("retrieving user {}", id);
        return userRepository.findById(id);
    }
}
