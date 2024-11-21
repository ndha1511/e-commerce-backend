package com.code.ecommercebackend.fakeData;

import com.code.ecommercebackend.models.Category;
import com.code.ecommercebackend.models.User;
import com.code.ecommercebackend.models.enums.Role;
import com.code.ecommercebackend.repositories.CategoryRepository;
import com.code.ecommercebackend.repositories.UserRepository;
import com.code.ecommercebackend.services.auth.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Configuration
@RequiredArgsConstructor
public class FakeData {
    private final AuthService authService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    public void fakeUsers() {
        String filePath = "fake-data/vietnamese_users.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            long numId = 2;
            List<User> users = new ArrayList<>();
            // admin
            User userAdmin = new User();
            userAdmin.setRoles(Set.of(Role.ROLE_ADMIN));
            userAdmin.setName("Admin");
            userAdmin.setPassword(passwordEncoder.encode("admin"));
            userAdmin.setEmail("admin@gmail.com");
            userAdmin.setVerify(true);
            userAdmin.setNumId(1L);
            userAdmin.setUsername("admin");
            users.add(userAdmin);


            while ((line = reader.readLine()) != null) {
                String name = line.split(",")[0].trim();
                String email = line.split(",")[1].trim();
                String password = passwordEncoder.encode("1");

                User user = new User();
                user.setRoles(Set.of(Role.ROLE_USER));
                user.setName(name);
                user.setEmail(email);
                user.setPassword(password);
                user.setNumId(numId);
                user.setVerify(true);
                user.generateUsername();
                users.add(user);
                numId++;
            }
            userRepository.saveAll(users);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void fakeCategories() {
        String filePath = "fake-data/categories.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            long numId = 1;
            List<Category> categories = new ArrayList<>();
            // admin



            while ((line = reader.readLine()) != null) {
                String name = line.split(",")[0].trim();
                String image = line.split(",")[1].trim();
                Category category = new Category();
                category.setCategoryName(name);
                category.setImage(image);
                category.setNumId(numId);
                category.createUrlPath();
                categories.add(category);

                numId++;
            }
           categoryRepository.saveAll(categories);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    @Bean
    public CommandLineRunner commandLineRunner(){
        return args -> {
//            fakeCategories();
        };
    }


}
