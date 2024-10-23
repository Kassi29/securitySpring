package com.kass.backend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePasswordRequest {
    private String email;
    private String currentPassword;
    private String newPassword;
    private String repeatNewPassword;
    private Integer userId;
}
