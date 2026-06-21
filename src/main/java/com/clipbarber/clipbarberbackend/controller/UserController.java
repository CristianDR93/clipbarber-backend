package com.clipbarber.clipbarberbackend.controller;

import com.clipbarber.clipbarberbackend.dto.LoginRequest;
import com.clipbarber.clipbarberbackend.dto.LoginResponse;
import com.clipbarber.clipbarberbackend.dto.RegisterRequest;
import com.clipbarber.clipbarberbackend.dto.UserResponse;
import com.clipbarber.clipbarberbackend.model.User;
import com.clipbarber.clipbarberbackend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

     private final UserService userService;

     public UserController(UserService userService){
         this.userService = userService;
     }

     @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest request){
         try{
             User createdUser = userService.registerUser(request);
             return ResponseEntity.ok(UserResponse.fromUser(createdUser));
         } catch (RuntimeException e){
             return ResponseEntity.badRequest().body(e.getMessage());
         }
     }

     @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request){
         try{
             LoginResponse loginResponse = userService.login(request.getEmail(), request.getPassword());
             return ResponseEntity.ok(loginResponse);
         } catch (RuntimeException e){
             return ResponseEntity.badRequest().body(e.getMessage());
         }
     }

     @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers(){
         List<UserResponse> users = userService.getAllUsers().stream()
                 .map(UserResponse::fromUser)
                 .toList();
         return ResponseEntity.ok(users);
     }

     @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id){
         return userService.getUserById(id)
                 .map(user -> ResponseEntity.ok((Object) UserResponse.fromUser(user)))
                 .orElse(ResponseEntity.notFound().build());
     }

}
