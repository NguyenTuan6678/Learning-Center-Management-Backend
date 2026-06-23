package com.example.TanKhoaLearningCenterBE.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails; // Import UserDetails
import java.util.Optional;

@Configuration
@EnableMongoAuditing
public class AuditConfiguration {

    @Bean
    public AuditorAware<String> auditorAware() {
        return () -> {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                return Optional.empty();
            }

            Object principal = authentication.getPrincipal(); // Get the principal

            if (principal instanceof UserDetails) {
                UserDetails userDetails = (UserDetails) principal;
                // Assuming your UserDetails implementation is AccountEntity
                // and you want to store accountId as a String.
                return Optional.of(userDetails.getUsername()); // Or userDetails.getAccountId().toString()
            }
            else if (principal instanceof String) {
                return Optional.of((String) principal);
            }
            // If the principal is just the username (String), you can use that directly:

            return Optional.empty();
        };
    }
}
