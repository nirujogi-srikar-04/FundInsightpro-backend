package com.fundinsight.backend.dto;

import com.fundinsight.backend.model.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    private String name;
    private String email;
    private String otp;
    private String password;
    private Integer age;
    private String phoneNumber;
    private Role role;
}
