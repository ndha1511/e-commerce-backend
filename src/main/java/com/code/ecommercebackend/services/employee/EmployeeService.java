package com.code.ecommercebackend.services.employee;

import com.code.ecommercebackend.dtos.request.employee.EmployeeAccount;
import com.code.ecommercebackend.exceptions.DataNotFoundException;
import com.code.ecommercebackend.models.User;


public interface EmployeeService {
    User createAccountEmployee(EmployeeAccount accountEmployee);
    void deleteAccountEmployee(String email) throws DataNotFoundException;
}
