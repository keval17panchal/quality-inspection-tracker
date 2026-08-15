package com.qc.inspection.dto;

import com.qc.inspection.model.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class UpdateUserAdminRequest {

    @NotBlank(message = "Full Name is required")
    private String name;

    @NotNull(message = "Role is required")
    private Role role;

    private String password;

    public UpdateUserAdminRequest() {}

    public UpdateUserAdminRequest(String name, Role role, String password) {
        this.name = name;
        this.role = role;
        this.password = password;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
