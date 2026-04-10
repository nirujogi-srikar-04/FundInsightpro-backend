package com.fundinsight.backend.config;

import com.fundinsight.backend.model.EducationalArticle;
import com.fundinsight.backend.model.MutualFund;
import com.fundinsight.backend.model.Role;
import com.fundinsight.backend.model.User;
import com.fundinsight.backend.repository.EducationalArticleRepository;
import com.fundinsight.backend.repository.MutualFundRepository;
import com.fundinsight.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataSeeder implements CommandLineRunner {

    private final MutualFundRepository mutualFundRepository;
    private final EducationalArticleRepository articleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public DataSeeder(MutualFundRepository mutualFundRepository,
                      EducationalArticleRepository articleRepository,
                      UserRepository userRepository,
                      PasswordEncoder passwordEncoder) {
        this.mutualFundRepository = mutualFundRepository;
        this.articleRepository = articleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {

        // Seed demo users
        if (userRepository.count() == 0) {
            userRepository.saveAll(List.of(
                User.builder()
                    .name("Admin User")
                    .email("admin@fundinsight.com")
                    .password(passwordEncoder.encode("admin123"))
                    .role(Role.ADMIN)
                    .build(),
                User.builder()
                    .name("Jane Advisor")
                    .email("advisor@fundinsight.com")
                    .password(passwordEncoder.encode("advisor123"))
                    .role(Role.ADVISOR)
                    .build(),
                User.builder()
                    .name("John Investor")
                    .email("user@fundinsight.com")
                    .password(passwordEncoder.encode("user123"))
                    .role(Role.USER)
                    .build()
            ));
            System.out.println("Demo users seeded.");
        }

        // Seed mutual funds
        if (mutualFundRepository.count() == 0) {
            mutualFundRepository.saveAll(List.of(
                new MutualFund("MF001", "HDFC Top 100 Fund", "Equity", 18.5, 22.3, 19.8, "High", 25000.0, 1.8, 5000.0, 5, 789.45, "1% if redeemed within 1 year"),
                new MutualFund("MF002", "SBI Bluechip Fund", "Equity", 16.2, 20.1, 18.5, "High", 32000.0, 1.6, 5000.0, 4, 654.32, "1% if redeemed within 1 year"),
                new MutualFund("MF003", "ICICI Prudential Balanced Advantage", "Hybrid", 14.8, 16.5, 15.2, "Medium", 28000.0, 1.9, 5000.0, 5, 456.78, "Nil"),
                new MutualFund("MF004", "Axis Midcap Fund", "Equity", 25.4, 28.7, 24.3, "Very High", 18000.0, 2.0, 5000.0, 5, 892.15, "1% if redeemed within 1 year"),
                new MutualFund("MF005", "Kotak Corporate Bond Fund", "Debt", 7.2, 7.8, 7.5, "Low", 15000.0, 0.9, 5000.0, 4, 234.56, "Nil")
            ));
            System.out.println("Mutual fund data seeded.");
        }

        // Seed educational articles
        if (articleRepository.count() == 0) {
            articleRepository.saveAll(List.of(
                new EducationalArticle("ART001", "Understanding Risk in Mutual Fund Investments", "Risk Management",
                    "Risk assessment is crucial for mutual fund selection. Different categories carry varying levels of volatility and potential returns.",
                    "Based on historical data analysis, diversifying across risk levels reduces portfolio volatility by approximately 35-40%.",
                    0.92, "5 min", "finance risk analysis"),
                new EducationalArticle("ART002", "Equity vs Debt vs Hybrid: Choosing Your Path", "Fund Categories",
                    "Understanding the fundamental differences between fund categories helps align investments with financial goals and risk appetite.",
                    "Machine learning analysis of 10,000+ investor profiles shows that hybrid funds are optimal for moderate risk tolerance with 3-5 year horizons.",
                    0.88, "7 min", "investment portfolio balance")
            ));
            System.out.println("Educational articles seeded.");
        }
    }
}
