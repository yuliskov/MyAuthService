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

package org.yuliskov.myauthservice.endpoint;

import org.yuliskov.myauthservice.exception.*;
import org.yuliskov.myauthservice.model.InstaUserDetails;
import org.yuliskov.myauthservice.model.User;
import org.yuliskov.myauthservice.payload.*;
import org.yuliskov.myauthservice.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@Slf4j
public class UserEndpoint {

    @Autowired
    private UserService userService;

    @GetMapping(value = "/users/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findUser(@PathVariable("username") String username) {
        log.info("retrieving user {}", username);

        return  userService
                .findByUsername(username)
                .map(user -> ResponseEntity.ok(user))
                .orElseThrow(() -> new ResourceNotFoundException(username));
    }

    @GetMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findAll() {
        log.info("retrieving all users");

        return ResponseEntity
                .ok(userService.findAll());
    }

    @GetMapping(value = "/users/summaries", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findAllUserSummaries(
            @AuthenticationPrincipal InstaUserDetails userDetails) {
        log.info("retrieving all users summaries");

        return ResponseEntity.ok(userService
                .findAll()
                .stream()
                .filter(user -> !user.getUsername().equals(userDetails.getUsername()))
                .map(this::convertTo));
    }

    @GetMapping(value = "/users/me", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('USER') or hasRole('FACEBOOK_USER')")
    @ResponseStatus(HttpStatus.OK)
    public UserSummary getCurrentUser(@AuthenticationPrincipal InstaUserDetails userDetails) {
        return UserSummary
                .builder()
                .id(userDetails.getId())
                .username(userDetails.getUsername())
                .name(userDetails.getUserProfile().getDisplayName())
                .profilePicture(userDetails.getUserProfile().getProfilePictureUrl())
                .build();
    }

    @GetMapping(value = "/users/summary/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getUserSummary(@PathVariable("username") String username) {
        log.info("retrieving user {}", username);

        return  userService
                .findByUsername(username)
                .map(user -> ResponseEntity.ok(convertTo(user)))
                .orElseThrow(() -> new ResourceNotFoundException(username));
    }

    private UserSummary convertTo(User user) {
        return UserSummary
                .builder()
                .id(user.getId())
                .username(user.getUsername())
                .name(user.getUserProfile().getDisplayName())
                .profilePicture(user.getUserProfile().getProfilePictureUrl())
                .build();
    }
}