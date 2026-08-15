package com.qc.inspection.dto;

import com.qc.inspection.model.Role;

public class UserResponse {

    private Long id;
    private String username;
    private String name;
    private Role role;

    public UserResponse() {}

    public UserResponse(Long id, String username, String name, Role role) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.role = role;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
}
