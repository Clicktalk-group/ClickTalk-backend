package com.greta.clicktalk.DTOs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UpdatePasswordRequestDTO {
    private String currentPassword;
    @NotBlank(message = "password must be 6 chat it lest")
    @Size(min = 6, max = 20, message = "the password must be between 6 and 20 char")
    private String newPassword;

    public UpdatePasswordRequestDTO(String currentPassword, String newPassword) {
        this.currentPassword = currentPassword;
        this.newPassword = newPassword;
    }

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
