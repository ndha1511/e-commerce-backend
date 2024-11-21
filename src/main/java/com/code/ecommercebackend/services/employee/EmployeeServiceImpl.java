package com.code.ecommercebackend.services.employee;

import com.code.ecommercebackend.dtos.request.employee.EmployeeAccount;
import com.code.ecommercebackend.exceptions.DataNotFoundException;
import com.code.ecommercebackend.models.User;
import com.code.ecommercebackend.models.enums.Role;
import com.code.ecommercebackend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;


@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Override
    public User createAccountEmployee(EmployeeAccount accountEmployee) {
        User user = new User();
        user.generateUsername();
        user.setVerify(true);
        user.setNumId(userRepository.count() + 1);
        user.setPassword(passwordEncoder.encode(accountEmployee.getPassword()));
        user.setEmail(accountEmployee.getEmail());
        user.setRoles(Set.of(Role.ROLE_EMPLOYEE));
        return userRepository.save(user);
    }

    @Override
    public void deleteAccountEmployee(String email) throws DataNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new DataNotFoundException("User with email " + email + " not found"));
        user.setInactive(true);
        userRepository.save(user);
    }


}
