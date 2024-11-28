package com.code.ecommercebackend.fakeData;

import com.code.ecommercebackend.dtos.request.payment.OrderItem;
import com.code.ecommercebackend.dtos.request.payment.OrderRequest;
import com.code.ecommercebackend.exceptions.DataNotFoundException;
import com.code.ecommercebackend.models.*;
import com.code.ecommercebackend.models.enums.OrderStatus;
import com.code.ecommercebackend.models.enums.PaymentMethod;
import com.code.ecommercebackend.models.enums.Role;
import com.code.ecommercebackend.repositories.*;
import com.code.ecommercebackend.services.auth.AuthService;
import com.code.ecommercebackend.services.payment.PaymentServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

@Configuration
@RequiredArgsConstructor
public class FakeData {
    private final AuthService authService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final ProductFeatureRepository userBehaviorRepository;
    private final BrandRepository brandRepository;
    private final ProductFeatureRepository productFeatureRepository;
    private final VariantRepository variantRepository;
    private final PaymentServiceImpl paymentServiceImpl;
    private final OrderRepository orderRepository;

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

    public void fakeUserBehavior() {
        List<Product> products = productRepository.findAll();
        Random rand = new Random();
        for (Product product : products) {
            ProductFeature productFeature = new ProductFeature();
            productFeature.setProductId(product.getNumId());
            productFeature.setProductName(product.getProductName());
            brandRepository.findById(product.getBrandId()).ifPresent(brand -> productFeature.setBrand(brand.getBrandName()));

            List<String> categories = new ArrayList<>(product.getCategories());
            categoryRepository.findById(categories.get(categories.size() - 1)).ifPresent(category -> productFeature.setCategory(category.getCategoryName()));
            productFeature.setPrice(product.getRegularPrice());
            productFeature.setCountView(rand.nextInt(100) + 1);
            productFeatureRepository.save(productFeature);
        }
    }

    public void updateOrder() {
        List<Order> orders = orderRepository.findAll();
        for (Order order : orders) {
            order.setOrderStatus(OrderStatus.RECEIVED);
        }
        orderRepository.saveAll(orders);
    }



//    @Bean
    public CommandLineRunner commandLineRunner(){
        return args -> {
//            fakeCategories();
//            fakeUserBehavior();
//            updateOrder();
        };
    }


}
