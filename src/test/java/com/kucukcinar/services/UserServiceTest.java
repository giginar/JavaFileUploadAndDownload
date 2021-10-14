package com.kucukcinar.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.kucukcinar.entities.User;
import com.kucukcinar.entities.UserRole;
import com.kucukcinar.repositories.UserRepository;
import com.kucukcinar.requests.login.LoginRequest;
import com.kucukcinar.requests.registration.RegistrationRequest;
import com.kucukcinar.responses.JwtResponse;
import com.kucukcinar.security.jwt.JwtUtils;

import java.util.ArrayList;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {UserService.class, JwtUtils.class})
@ExtendWith(SpringExtension.class)
class UserServiceTest {
    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private JwtUtils jwtUtils;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Test
    void testAuthenticateUser() throws AuthenticationException {
        when(this.jwtUtils.generateJwtToken((Authentication) any())).thenThrow(new UsernameNotFoundException("Msg"));
        when(this.authenticationManager.authenticate((Authentication) any()))
                .thenReturn(new TestingAuthenticationToken("Principal", "Credentials"));
        assertThrows(UsernameNotFoundException.class,
                () -> this.userService.authenticateUser(new LoginRequest("jane.doe@example.org", "iloveyou")));
        verify(this.jwtUtils).generateJwtToken((Authentication) any());
        verify(this.authenticationManager).authenticate((Authentication) any());
    }

    @Test
    void testAuthenticateUser2() throws AuthenticationException {
        when(this.jwtUtils.generateJwtToken((Authentication) any())).thenReturn("ABC123");

        User user = new User();
        user.setLastName("Doe");
        user.setEmail("jane.doe@example.org");
        user.setPassword("iloveyou");
        user.setUserRole(UserRole.USER);
        user.setId(1);
        user.setFirstName("Jane");
        TestingAuthenticationToken testingAuthenticationToken = new TestingAuthenticationToken(user, "Credentials");

        when(this.authenticationManager.authenticate((Authentication) any())).thenReturn(testingAuthenticationToken);
        ResponseEntity<?> actualAuthenticateUserResult = this.userService
                .authenticateUser(new LoginRequest("jane.doe@example.org", "iloveyou"));
        assertTrue(actualAuthenticateUserResult.hasBody());
        assertTrue(actualAuthenticateUserResult.getHeaders().isEmpty());
        assertEquals(HttpStatus.OK, actualAuthenticateUserResult.getStatusCode());
        assertEquals("jane.doe@example.org", ((JwtResponse) actualAuthenticateUserResult.getBody()).getEmail());
        assertEquals("ABC123", ((JwtResponse) actualAuthenticateUserResult.getBody()).getAccessToken());
        assertEquals("Bearer", ((JwtResponse) actualAuthenticateUserResult.getBody()).getTokenType());
        assertEquals(1, ((JwtResponse) actualAuthenticateUserResult.getBody()).getId().intValue());
        assertEquals("USER", ((JwtResponse) actualAuthenticateUserResult.getBody()).getRole());
        verify(this.jwtUtils).generateJwtToken((Authentication) any());
        verify(this.authenticationManager).authenticate((Authentication) any());
    }

    @Test
    void testAuthenticateUser3() throws AuthenticationException {
        when(this.jwtUtils.generateJwtToken((Authentication) any())).thenReturn("ABC123");
        User user = mock(User.class);
        when(user.getEmail()).thenReturn("jane.doe@example.org");
        when(user.getId()).thenReturn(1);
        TestingAuthenticationToken testingAuthenticationToken = new TestingAuthenticationToken(user, "Credentials");

        when(this.authenticationManager.authenticate((Authentication) any())).thenReturn(testingAuthenticationToken);
        ResponseEntity<?> actualAuthenticateUserResult = this.userService
                .authenticateUser(new LoginRequest("jane.doe@example.org", "iloveyou"));
        assertTrue(actualAuthenticateUserResult.hasBody());
        assertTrue(actualAuthenticateUserResult.getHeaders().isEmpty());
        assertEquals(HttpStatus.OK, actualAuthenticateUserResult.getStatusCode());
        assertEquals("jane.doe@example.org", ((JwtResponse) actualAuthenticateUserResult.getBody()).getEmail());
        assertEquals("ABC123", ((JwtResponse) actualAuthenticateUserResult.getBody()).getAccessToken());
        assertEquals("Bearer", ((JwtResponse) actualAuthenticateUserResult.getBody()).getTokenType());
        assertEquals(1, ((JwtResponse) actualAuthenticateUserResult.getBody()).getId().intValue());
        assertEquals("USER", ((JwtResponse) actualAuthenticateUserResult.getBody()).getRole());
        verify(this.jwtUtils).generateJwtToken((Authentication) any());
        verify(this.authenticationManager).authenticate((Authentication) any());
        verify(user).getEmail();
        verify(user).getId();
    }

    @Test
    void testRegisterUser() {
        User user = new User();
        user.setLastName("Doe");
        user.setEmail("jane.doe@example.org");
        user.setPassword("iloveyou");
        user.setUserRole(UserRole.USER);
        user.setId(1);
        user.setFirstName("Jane");
        when(this.userRepository.save((User) any())).thenReturn(user);
        when(this.userRepository.findAll()).thenReturn(new ArrayList<User>());
        when(this.passwordEncoder.encode((CharSequence) any())).thenReturn("secret");
        ResponseEntity<?> actualRegisterUserResult = this.userService
                .registerUser(new RegistrationRequest("Jane", "Doe", "iloveyou", "jane.doe@example.org"));
        assertEquals("User registered successfully!", actualRegisterUserResult.getBody());
        assertEquals("<200 OK OK,User registered successfully!,[]>", actualRegisterUserResult.toString());
        assertEquals(HttpStatus.OK, actualRegisterUserResult.getStatusCode());
        assertTrue(actualRegisterUserResult.getHeaders().isEmpty());
        verify(this.userRepository).findAll();
        verify(this.userRepository).save((User) any());
        verify(this.passwordEncoder).encode((CharSequence) any());
    }

    @Test
    void testRegisterUser2() {
        User user = new User();
        user.setLastName("Doe");
        user.setEmail("jane.doe@example.org");
        user.setPassword("iloveyou");
        user.setUserRole(UserRole.USER);
        user.setId(1);
        user.setFirstName("Jane");

        ArrayList<User> userList = new ArrayList<User>();
        userList.add(user);

        User user1 = new User();
        user1.setLastName("Doe");
        user1.setEmail("jane.doe@example.org");
        user1.setPassword("iloveyou");
        user1.setUserRole(UserRole.USER);
        user1.setId(1);
        user1.setFirstName("Jane");
        when(this.userRepository.save((User) any())).thenReturn(user1);
        when(this.userRepository.findAll()).thenReturn(userList);
        when(this.passwordEncoder.encode((CharSequence) any())).thenReturn("secret");
        ResponseEntity<?> actualRegisterUserResult = this.userService
                .registerUser(new RegistrationRequest("Jane", "Doe", "iloveyou", "jane.doe@example.org"));
        assertEquals("Email is in use.", actualRegisterUserResult.getBody());
        assertEquals("<400 BAD_REQUEST Bad Request,Email is in use.,[]>", actualRegisterUserResult.toString());
        assertEquals(HttpStatus.BAD_REQUEST, actualRegisterUserResult.getStatusCode());
        assertTrue(actualRegisterUserResult.getHeaders().isEmpty());
        verify(this.userRepository).findAll();
    }

    @Test
    void testLoadUserByUsername() throws UsernameNotFoundException {
        User user = new User();
        user.setLastName("Doe");
        user.setEmail("jane.doe@example.org");
        user.setPassword("iloveyou");
        user.setUserRole(UserRole.USER);
        user.setId(1);
        user.setFirstName("Jane");
        Optional<User> ofResult = Optional.<User>of(user);
        when(this.userRepository.findByEmail((String) any())).thenReturn(ofResult);
        UserDetails actualLoadUserByUsernameResult = this.userService.loadUserByUsername("jane.doe@example.org");
        assertEquals("jane.doe@example.org", ((User) actualLoadUserByUsernameResult).getEmail());
        assertEquals(UserRole.USER, ((User) actualLoadUserByUsernameResult).getUserRole());
        assertEquals("iloveyou", actualLoadUserByUsernameResult.getPassword());
        assertEquals("Doe", ((User) actualLoadUserByUsernameResult).getLastName());
        assertEquals("Jane", ((User) actualLoadUserByUsernameResult).getFirstName());
        verify(this.userRepository).findByEmail((String) any());
    }

    @Test
    void testLoadUserByUsername2() throws UsernameNotFoundException {
        when(this.userRepository.findByEmail((String) any())).thenReturn(Optional.<User>empty());
        assertThrows(UsernameNotFoundException.class, () -> this.userService.loadUserByUsername("jane.doe@example.org"));
        verify(this.userRepository).findByEmail((String) any());
    }
}

