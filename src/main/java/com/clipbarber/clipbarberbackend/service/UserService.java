package com.clipbarber.clipbarberbackend.service;

import com.clipbarber.clipbarberbackend.dto.LoginResponse;
import com.clipbarber.clipbarberbackend.dto.RegisterRequest;
import com.clipbarber.clipbarberbackend.model.User;
import com.clipbarber.clipbarberbackend.repository.UserRepository;
import com.clipbarber.clipbarberbackend.security.JwtUtil;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder, JwtUtil jwtUtil){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public User registerUser(RegisterRequest request){

        Optional<User> existingUser = userRepository.findByEmail(request.getEmail());

        if (existingUser.isPresent()){
            throw new RuntimeException("Error: Email ya esta registrado");
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setPhone(request.getPhone());
        user.setRol(request.getRol() != null ? request.getRol() : User.Rol.CLIENTE);

        return userRepository.save(user);
    }

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id){
        return userRepository.findById(id);
    }

    public LoginResponse login(String email, String password){

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Error: Credenciales invalidas"));

        if (!passwordEncoder.matches(password, user.getPassword())){
            throw new RuntimeException("Error: Credenciales invalidas");
        }

        String token = jwtUtil.generateToken(user.getId(), user.getEmail(), user.getRol().name());

        return new LoginResponse(
                token,
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPhone(),
                user.getRol().name()
        );
    }
}
