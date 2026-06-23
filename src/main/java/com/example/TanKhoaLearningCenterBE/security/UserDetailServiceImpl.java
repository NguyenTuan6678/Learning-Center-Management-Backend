package com.example.TanKhoaLearningCenterBE.security;

import com.example.TanKhoaLearningCenterBE.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {
    private final AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var userAccountEntity = accountRepository.findByUserNameContainingIgnoreCase(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));

        log.info("******** loadUserByUsername:{}", userAccountEntity);

        return new User(userAccountEntity.getUsername(), userAccountEntity.getPassword(), true
                , true, true, true, userAccountEntity.getAuthorities());
    }
}
