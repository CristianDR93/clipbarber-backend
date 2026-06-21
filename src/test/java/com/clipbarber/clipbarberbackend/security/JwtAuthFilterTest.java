package com.clipbarber.clipbarberbackend.security;

import com.clipbarber.clipbarberbackend.model.User;
import com.clipbarber.clipbarberbackend.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtAuthFilterTest {

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private UserRepository userRepository;

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private JwtAuthFilter jwtAuthFilter;

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private User user;

    @BeforeEach
    void setUp() {
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        SecurityContextHolder.clearContext();

        user = new User();
        user.setId(1L);
        user.setName("Juan Perez");
        user.setEmail("juan@example.com");
        user.setPassword("hashed");
        user.setPhone("+56912345678");
        user.setRol(User.Rol.CLIENTE);
    }

    @Test
    void doFilterInternal_ShouldSetAuthentication_WhenValidToken() throws ServletException, IOException {
        request.addHeader("Authorization", "Bearer valid-token");
        when(jwtUtil.isTokenValid("valid-token")).thenReturn(true);
        when(jwtUtil.extractEmail("valid-token")).thenReturn("juan@example.com");
        when(userRepository.findByEmail("juan@example.com")).thenReturn(Optional.of(user));

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        assertEquals(user, SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void doFilterInternal_ShouldNotSetAuthentication_WhenNoAuthorizationHeader() throws ServletException, IOException {
        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void doFilterInternal_ShouldNotSetAuthentication_WhenHeaderDoesNotStartWithBearer() throws ServletException, IOException {
        request.addHeader("Authorization", "Basic abc123");

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void doFilterInternal_ShouldNotSetAuthentication_WhenTokenIsInvalid() throws ServletException, IOException {
        request.addHeader("Authorization", "Bearer invalid-token");
        when(jwtUtil.isTokenValid("invalid-token")).thenReturn(false);

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void doFilterInternal_ShouldNotSetAuthentication_WhenUserNotFound() throws ServletException, IOException {
        request.addHeader("Authorization", "Bearer valid-token");
        when(jwtUtil.isTokenValid("valid-token")).thenReturn(true);
        when(jwtUtil.extractEmail("valid-token")).thenReturn("unknown@example.com");
        when(userRepository.findByEmail("unknown@example.com")).thenReturn(Optional.empty());

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void doFilterInternal_ShouldSetCorrectAuthority_WhenUserHasBarberoRole() throws ServletException, IOException {
        user.setRol(User.Rol.BARBERO);
        request.addHeader("Authorization", "Bearer valid-token");
        when(jwtUtil.isTokenValid("valid-token")).thenReturn(true);
        when(jwtUtil.extractEmail("valid-token")).thenReturn("juan@example.com");
        when(userRepository.findByEmail("juan@example.com")).thenReturn(Optional.of(user));

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        assertEquals("ROLE_BARBERO",
                SecurityContextHolder.getContext().getAuthentication().getAuthorities().iterator().next().getAuthority());
    }
}
