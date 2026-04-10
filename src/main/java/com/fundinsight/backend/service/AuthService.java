package com.fundinsight.backend.service;

import com.fundinsight.backend.dto.AuthRequest;
import com.fundinsight.backend.dto.AuthResponse;
import com.fundinsight.backend.dto.OtpRequest;
import com.fundinsight.backend.dto.RegisterRequest;
import com.fundinsight.backend.model.OtpVerification;
import com.fundinsight.backend.model.Role;
import com.fundinsight.backend.model.User;
import com.fundinsight.backend.repository.OtpRepository;
import com.fundinsight.backend.repository.UserRepository;
import com.fundinsight.backend.security.JwtService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class AuthService {

    private final UserRepository repository;
    private final OtpRepository otpRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;

    @Autowired
    public AuthService(UserRepository repository,
                       OtpRepository otpRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService,
                       AuthenticationManager authenticationManager,
                       EmailService emailService) {
        this.repository = repository;
        this.otpRepository = otpRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.emailService = emailService;
    }

    @Transactional
    public void generateAndSendOtp(OtpRequest request) {
        if (repository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("An account with this email already exists.");
        }

        // Generate a secure 6-digit OTP
        String otp = String.format("%06d", new Random().nextInt(999999));

        // Delete any existing OTP for this email (re-send scenario)
        otpRepository.findByEmail(request.getEmail()).ifPresent(otpRepository::delete);

        // Store OTP with 10-minute expiry
        OtpVerification otpVerification = OtpVerification.builder()
                .email(request.getEmail())
                .otp(otp)
                .expirationTime(LocalDateTime.now().plusMinutes(10))
                .build();
        otpRepository.save(otpVerification);

        // Send real email (async - non-blocking)
        emailService.sendOtpEmail(request.getEmail(), otp);

        System.out.println("[AuthService] OTP generated for: " + request.getEmail() + " | OTP: " + otp);
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        OtpVerification otpRecord = otpRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("OTP not found. Please request a new OTP."));

        if (!otpRecord.getOtp().equals(request.getOtp())) {
            throw new RuntimeException("Invalid OTP. Please check your email and try again.");
        }

        if (otpRecord.getExpirationTime().isBefore(LocalDateTime.now())) {
            otpRepository.delete(otpRecord);
            throw new RuntimeException("OTP has expired. Please request a new one.");
        }

        var user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .age(request.getAge())
                .phoneNumber(request.getPhoneNumber())
                .role(request.getRole() != null ? request.getRole() : Role.USER)
                .build();

        repository.save(user);
        otpRepository.delete(otpRecord); // Clean up used OTP immediately

        var jwtToken = jwtService.generateToken(user);
        return AuthResponse.builder()
                .token(jwtToken)
                .user(AuthResponse.UserData.fromUser(user))
                .build();
    }

    public AuthResponse authenticate(AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = repository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found."));
        var jwtToken = jwtService.generateToken(user);
        return AuthResponse.builder()
                .token(jwtToken)
                .user(AuthResponse.UserData.fromUser(user))
                .build();
    }
}
