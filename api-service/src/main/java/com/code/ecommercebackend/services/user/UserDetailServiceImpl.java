package com.code.ecommercebackend.services.user;

import com.code.ecommercebackend.models.UserDetail;
import com.code.ecommercebackend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailServiceImpl implements UserDetailService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return new UserDetail(userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("user "+ username +" not found")));
    }
}
