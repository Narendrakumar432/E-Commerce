package com.ecommerce_backend.dto.response;

// This is what we send BACK to client after login/register
// {
//   "token": "eyJhbGciOiJIUzI1NiJ9...",
//   "email": "narendra@gmail.com",
//   "name": "Narendra",
//   "role": "USER"
// }
public class AuthResponse {

    private String token;
    private String email;
    private String name;
    private String role;

    // Constructor
    public AuthResponse(String token, String email, String name, String role) {
        this.token = token;
        this.email = email;
        this.name = name;
        this.role = role;
    }

    // Getters
    public String getToken() { return token; }
    public String getEmail() { return email; }
    public String getName() { return name; }
    public String getRole() { return role; }

    // Setters
    public void setToken(String token) { this.token = token; }
    public void setEmail(String email) { this.email = email; }
    public void setName(String name) { this.name = name; }
    public void setRole(String role) { this.role = role; }
}