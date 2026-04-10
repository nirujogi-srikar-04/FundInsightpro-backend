package com.fundinsight.backend.dto;

import com.fundinsight.backend.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {
    private String token;
    private UserData user;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserData {
        private Long id;
        private String name;
        private String email;
        private String role;
        private Integer age;
        private String phoneNumber;
        
        public static UserData fromUser(User user) {
            return UserData.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole().name())
                .age(user.getAge())
                .phoneNumber(user.getPhoneNumber())
                .build();
        }
    }
}
