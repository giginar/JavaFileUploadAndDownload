package com.kucukcinar.services;

import com.kucukcinar.entities.User;
import com.kucukcinar.entities.UserRole;
import com.kucukcinar.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import com.kucukcinar.requests.registration.RegistrationRequest;
import com.kucukcinar.responses.JwtResponse;
import com.kucukcinar.security.jwt.JwtUtils;
import com.kucukcinar.requests.login.LoginRequest;

import java.util.List;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtUtils jwtUtils;

    public ResponseEntity<?> authenticateUser(LoginRequest loginRequest) throws UsernameNotFoundException {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        User userDetails = (User) authentication.getPrincipal();

        return ResponseEntity.ok(
                new JwtResponse(jwt, userDetails.getId(), userDetails.getEmail(), "USER"));
    }


    public ResponseEntity<?> registerUser(RegistrationRequest registrationRequest) {
        List<User> users = userRepository.findAll();
        for (User us: users) {
            if (us.getEmail().equals(registrationRequest.getEmail())){
                return ResponseEntity.badRequest().body("Email is in use.");
            }
        }

        User appUser = new User(registrationRequest.getFirstName(), registrationRequest.getLastName(),
                registrationRequest.getEmail(), passwordEncoder.encode(registrationRequest.getPassword()),
                UserRole.USER);

        userRepository.save(appUser);

        return ResponseEntity.ok("User registered successfully!");
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + email));

        return User.build(user);
    }


}
