package com.fundinsight.backend.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromAddress;

    @Value("${app.mail.from-name:FundInsight Pro}")
    private String fromName;

    @Autowired
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Async
    public void sendOtpEmail(String toEmail, String otp) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromAddress, fromName);
            helper.setTo(toEmail);
            helper.setSubject("Your FundInsight Pro Verification Code: " + otp);
            helper.setText(buildOtpEmailHtml(otp), true); // true = HTML

            mailSender.send(message);
            System.out.println("[EmailService] OTP email sent successfully to: " + toEmail);

        } catch (MessagingException | java.io.UnsupportedEncodingException e) {
            System.err.println("[EmailService] Failed to send OTP email to " + toEmail + ": " + e.getMessage());
            throw new RuntimeException("Failed to send OTP email. Please try again.", e);
        }
    }

    private String buildOtpEmailHtml(String otp) {
        return """
            <!DOCTYPE html>
            <html lang="en">
            <head>
              <meta charset="UTF-8"/>
              <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
              <title>FundInsight OTP</title>
            </head>
            <body style="margin:0;padding:0;background-color:#0a0e27;font-family:'Segoe UI',Arial,sans-serif;">
              <table width="100%%" cellpadding="0" cellspacing="0" style="background-color:#0a0e27;padding:40px 20px;">
                <tr>
                  <td align="center">
                    <table width="600" cellpadding="0" cellspacing="0" style="background-color:#141829;border-radius:16px;overflow:hidden;box-shadow:0 8px 32px rgba(0,0,0,0.4);">
                      <!-- Header -->
                      <tr>
                        <td style="background:linear-gradient(135deg,#1a56db,#0a0e27);padding:36px 40px;text-align:center;">
                          <h1 style="color:#ffffff;margin:0;font-size:26px;font-weight:700;letter-spacing:-0.5px;">
                            FundInsight Pro
                          </h1>
                          <p style="color:rgba(255,255,255,0.7);margin:8px 0 0;font-size:14px;">
                            Secure Investment Platform
                          </p>
                        </td>
                      </tr>
                      <!-- Body -->
                      <tr>
                        <td style="padding:40px;">
                          <h2 style="color:#ffffff;margin:0 0 12px;font-size:22px;font-weight:600;">
                            Email Verification
                          </h2>
                          <p style="color:rgba(255,255,255,0.7);font-size:15px;line-height:1.6;margin:0 0 28px;">
                            You requested access to FundInsight Pro. Use the verification code below to complete your registration.
                            This code is valid for <strong style="color:#ffffff;">10 minutes</strong>.
                          </p>
                          <!-- OTP Box -->
                          <div style="background:#0a0e27;border:2px solid #1a56db;border-radius:12px;padding:28px;text-align:center;margin-bottom:28px;">
                            <p style="color:rgba(255,255,255,0.5);font-size:12px;text-transform:uppercase;letter-spacing:2px;margin:0 0 12px;">
                              Your Verification Code
                            </p>
                            <span style="color:#ffffff;font-size:42px;font-weight:700;letter-spacing:12px;font-family:monospace;">
                              %s
                            </span>
                          </div>
                          <p style="color:rgba(255,255,255,0.5);font-size:13px;line-height:1.6;margin:0;">
                            If you did not request this code, please ignore this email. Your account is safe.
                            Do not share this code with anyone.
                          </p>
                        </td>
                      </tr>
                      <!-- Footer -->
                      <tr>
                        <td style="background:#0a0e27;padding:24px 40px;border-top:1px solid rgba(255,255,255,0.05);">
                          <p style="color:rgba(255,255,255,0.3);font-size:12px;margin:0;text-align:center;">
                            &copy; 2026 FundInsight Pro. All rights reserved.<br/>
                            This is an automated message. Please do not reply.
                          </p>
                        </td>
                      </tr>
                    </table>
                  </td>
                </tr>
              </table>
            </body>
            </html>
            """.formatted(otp);
    }
}
