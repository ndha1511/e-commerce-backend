package com.code.ecommercebackend.services.auth;

import com.code.ecommercebackend.dtos.request.auth.LoginRequest;
import com.code.ecommercebackend.dtos.request.auth.RegisterRequest;
import com.code.ecommercebackend.dtos.response.auth.TokenResponse;
import com.code.ecommercebackend.exceptions.DataExistsException;
import com.code.ecommercebackend.exceptions.DataExpiredException;
import com.code.ecommercebackend.exceptions.DataNotFoundException;
import com.code.ecommercebackend.exceptions.DataNotMatchedException;
import com.code.ecommercebackend.models.Token;
import com.code.ecommercebackend.models.User;
import com.code.ecommercebackend.models.UserDetail;
import com.code.ecommercebackend.models.enums.Role;
import com.code.ecommercebackend.repositories.TokenRepository;
import com.code.ecommercebackend.repositories.UserRepository;
import com.code.ecommercebackend.utils.EmailDetails;
import com.code.ecommercebackend.utils.EmailSender;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Objects;
import java.util.Random;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final EmailSender emailSender;
    private final TokenRepository tokenRepository;
    private final AuthenticationManager authenticationManager;
    @Value("${jwt.expiryDate}")
    private long expiryDate;

    @Override
    public void register(RegisterRequest registerRequest) throws DataNotMatchedException, MessagingException, DataExistsException {
        if(!Objects.equals(registerRequest.getPassword(), registerRequest.getConfirmPassword()))
            throw new DataNotMatchedException("confirm password is not matched");
        if(userRepository.existsByEmail(registerRequest.getEmail()))
            throw new DataExistsException("email already exists");
        User user = User.builder()
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .otp(generateOtp())
                .username(generateUsername())
                .roles(Set.of(Role.ROLE_USER))
                .verify(false)
                .expiryDateOtp(LocalDateTime.now().plusMinutes(5))
                .build();
        sendMail(user);
        userRepository.save(user);
    }

    @Override
    public TokenResponse verifyEmail(String email, String otp) throws DataNotFoundException, DataNotMatchedException, DataExpiredException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new DataNotFoundException("email not found"));
        if(user.getVerify())
            throw new DataNotMatchedException("email already verified");
        if(!user.getOtp().equals(otp))
            throw new DataNotMatchedException("Otp không chính xác");
        if(user.getExpiryDateOtp().isBefore(LocalDateTime.now()))
            throw new DataExpiredException("Otp đã hết hạn");
        user.setVerify(true);
        userRepository.save(user);

        return saveToken(user);
    }

    @Override
    public TokenResponse login(LoginRequest loginRequest) throws DataNotFoundException {
        User user = userRepository.findByUsernameOrEmailOrPhoneNumber(loginRequest.getUsername())
                .orElseThrow(() -> new DataNotFoundException("user not found"));
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(),
                loginRequest.getPassword()));
        return saveToken(user);
    }

    @Override
    public void sendOtp(String email) throws DataNotFoundException, MessagingException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new DataNotFoundException("user not found"));
        user.setOtp(generateOtp());
        user.setExpiryDateOtp(LocalDateTime.now().plusMinutes(5));
        userRepository.save(user);
        sendMail(user);

    }

    @Override
    public void checkEmail(String email) throws DataExistsException {
        if(userRepository.existsByEmail(email))
            throw new DataExistsException("email already exists");
    }

    private TokenResponse saveToken(User user) {
        LocalDateTime exp = LocalDateTime.now().plusSeconds(expiryDate);
        UserDetail userDetail = new UserDetail(user);
        Token token = Token.builder()
                .accessToken(jwtService.generateToken(userDetail))
                .refreshToken(jwtService.generateRefreshToken(new HashMap<>(), userDetail))
                .userId(user.getId())
                .expiredDate(LocalDateTime.now().plusDays(15))
                .build();
        tokenRepository.save(token);
        return TokenResponse.builder()
                .accessToken(jwtService.generateToken(userDetail))
                .refreshToken(jwtService.generateRefreshToken(new HashMap<>(), userDetail))
                .expiresAt(exp)
                .build();
    }

    private void sendMail(User user) throws MessagingException {
        EmailDetails emailDetails = EmailDetails.builder()
                .subject("Đây là mã xác thực email của bạn")
                .msgBody(user.getOtp())
                .recipient(user.getEmail())
                .build();
        emailSender.sendHtmlMail(emailDetails);
    }

    private String generateOtp() {
        Random random = new Random();
        StringBuilder otp = new StringBuilder();
        for(int i = 0; i <= 5; i++) {
            otp.append(random.nextInt(10));
        }
        return otp.toString();
    }

    private String generateUsername() {
        String character = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder username = new StringBuilder();
        Random random = new Random();
        for(int i = 0; i <= 10; i++) {
            username.append(character.charAt(random.nextInt(character.length())));
        }
        return username.toString();
    }
}
