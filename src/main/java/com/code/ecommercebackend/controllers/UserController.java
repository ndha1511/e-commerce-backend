package com.code.ecommercebackend.controllers;

import com.code.ecommercebackend.dtos.request.user.UserDto;
import com.code.ecommercebackend.dtos.request.employee.EmployeeAccount;
import com.code.ecommercebackend.dtos.response.Response;
import com.code.ecommercebackend.dtos.response.ResponseSuccess;
import com.code.ecommercebackend.models.User;
import com.code.ecommercebackend.models.enums.Role;
import com.code.ecommercebackend.services.employee.EmployeeService;
import com.code.ecommercebackend.services.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("${api.prefix}/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final EmployeeService employeeService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    public Response getUserById(@PathVariable String id)
    throws Exception {
        return new ResponseSuccess<>(
                HttpStatus.OK.value(),
                "success",
                userService.findById(id)
        );
    }
    @PreAuthorize("isAuthenticated()")
    @PutMapping("/{email}")
    public Response updateUser(@PathVariable String email,@ModelAttribute UserDto userDto)
            throws Exception {
        return new ResponseSuccess<>(
                HttpStatus.OK.value(),
                "success",
                userService.update(email,userDto)
        );
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    @GetMapping
    public Response getAllUsers(@RequestParam(defaultValue = "1") int pageNo,
                                @RequestParam(defaultValue = "40") int size,
                                @RequestParam(required = false) String[] search,
                                @RequestParam(required = false) String[] sort) {
        List<String> searchList = new ArrayList<>();
        searchList.add("inactive=false");
        searchList.add("roles=" + Role.ROLE_USER);
        if(search != null) {
            searchList.addAll(Arrays.asList(search));
        }
        search = searchList.toArray(new String[0]);
        return new ResponseSuccess<>(
                HttpStatus.OK.value(),
                "success",
                userService.getPageData(pageNo, size, search, sort, User.class)
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/employee")
    public Response getEmployee(@RequestParam(defaultValue = "1") int pageNo,
                                @RequestParam(defaultValue = "40") int size,
                                @RequestParam(required = false) String[] search,
                                @RequestParam(required = false) String[] sort) {
        List<String> searchList = new ArrayList<>();
        searchList.add("inactive=false");
        searchList.add("roles=" + Role.ROLE_EMPLOYEE);
        if(search != null) {
            searchList.addAll(Arrays.asList(search));
        }
        search = searchList.toArray(new String[0]);
        return new ResponseSuccess<>(
                HttpStatus.OK.value(),
                "success",
                userService.getPageData(pageNo, size, search, sort, User.class)
        );

    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create-account-emp")
    public Response createAccount(@Valid @RequestBody EmployeeAccount employeeAccount) {
        return new ResponseSuccess<>(
                HttpStatus.OK.value(),
                "success",
                employeeService.createAccountEmployee(employeeAccount)
        );
    }

    @DeleteMapping("/{email}")
    public Response deleteAccount(@PathVariable String email)
    throws Exception {
        employeeService.deleteAccountEmployee(email);
        return new ResponseSuccess<>(
                HttpStatus.NO_CONTENT.value(),
                "success"
        );
    }


}
