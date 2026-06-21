package com.clipbarber.clipbarberbackend.dto;

import com.clipbarber.clipbarberbackend.model.User;

public class UserResponse {

    private Long id;
    private String name;
    private String email;
    private String phone;
    private String rol;

    public UserResponse() {}

    public UserResponse(Long id, String name, String email, String phone, String rol) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.rol = rol;
    }

    public static UserResponse fromUser(User user) {
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPhone(),
                user.getRol().name()
        );
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }
}
