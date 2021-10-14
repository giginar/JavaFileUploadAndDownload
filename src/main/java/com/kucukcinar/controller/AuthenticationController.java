package com.kucukcinar.controller;

import javax.validation.Valid;

import com.kucukcinar.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kucukcinar.requests.registration.RegistrationRequest;
import com.kucukcinar.requests.login.LoginRequest;

/**
 * The type Authentication controller.
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/welcome")
public class AuthenticationController {

    @Autowired
    UserService userService;

    /**
     * Authenticate user response entity.
     *
     * @param loginRequest the login request
     * @return the response entity
     */
    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        return userService.authenticateUser(loginRequest);
    }

    /**
     * Register user response entity.
     *
     * @param registrationRequest the registration request
     * @return the response entity
     */
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody RegistrationRequest registrationRequest){
        return userService.registerUser(registrationRequest);
    }

}
