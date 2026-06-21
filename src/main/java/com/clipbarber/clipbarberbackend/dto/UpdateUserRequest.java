package com.clipbarber.clipbarberbackend.dto;

import com.clipbarber.clipbarberbackend.model.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public class UpdateUserRequest {

    @Size(min = 2, message = "El nombre debe tener al menos 2 caracteres")
    private String name;

    @Email(message = "Formato de email invalido")
    private String email;

    @Size(min = 6, message = "La contrasena debe tener al menos 6 caracteres")
    private String password;

    private String phone;

    private User.Rol rol;

    public UpdateUserRequest() {}

    public UpdateUserRequest(String name, String email, String password, String phone, User.Rol rol) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.rol = rol;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public User.Rol getRol() {
        return rol;
    }

    public void setRol(User.Rol rol) {
        this.rol = rol;
    }
}
