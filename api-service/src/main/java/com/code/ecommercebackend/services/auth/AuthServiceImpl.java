package com.code.ecommercebackend.services.auth;

import com.code.ecommercebackend.dtos.request.auth.ChangePasswordRequest;
import com.code.ecommercebackend.dtos.request.auth.CreateNewPasswordRequest;
import com.code.ecommercebackend.dtos.request.auth.LoginRequest;
import com.code.ecommercebackend.dtos.request.auth.RegisterRequest;
import com.code.ecommercebackend.dtos.response.auth.TokenResponse;
import com.code.ecommercebackend.exceptions.*;
import com.code.ecommercebackend.models.Token;
import com.code.ecommercebackend.models.User;
import com.code.ecommercebackend.models.UserDetail;
import com.code.ecommercebackend.models.enums.Role;
import com.code.ecommercebackend.repositories.TokenRepository;
import com.code.ecommercebackend.repositories.UserRepository;
import com.code.ecommercebackend.utils.CookieHandler;
import com.code.ecommercebackend.utils.EmailDetails;
import com.code.ecommercebackend.utils.EmailSender;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final EmailSender emailSender;
    private final TokenRepository tokenRepository;
    private final AuthenticationManager authenticationManager;
    private final CookieHandler cookieHandler;

    @Override
    public void register(RegisterRequest registerRequest) throws DataNotMatchedException, MessagingException, DataExistsException {
        if(!Objects.equals(registerRequest.getPassword(), registerRequest.getConfirmPassword()))
            throw new DataNotMatchedException("confirm password is not matched");
        if(userRepository.existsByEmail(registerRequest.getEmail()))
            throw new DataExistsException("email already exists");
        User user = User.builder()
                .numId(userRepository.count() + 1)
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .otp(generateOtp())
                .roles(Set.of(Role.ROLE_USER))
                .verify(false)
                .expiryDateOtp(LocalDateTime.now().plusMinutes(5))
                .build();
        user.generateUsername();
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
            throw new DataNotMatchedException("otp không chính xác");
        if(user.getExpiryDateOtp().isBefore(LocalDateTime.now()))
            throw new DataExpiredException("otp đã hết hạn");
        user.setVerify(true);
        userRepository.save(user);

        return saveToken(user);
    }

    @Override
    public TokenResponse login(LoginRequest loginRequest) throws DataNotFoundException, UserNotVerifyException, MessagingException {
        User user = userRepository.findByUsernameOrEmailOrPhoneNumber(loginRequest.getUsername())
                .orElseThrow(() -> new DataNotFoundException("user not found"));
        if(!user.getVerify()) {
            user.setOtp(generateOtp());
            user.setExpiryDateOtp(LocalDateTime.now().plusMinutes(5));
            userRepository.save(user);
            sendMail(user);
            throw new UserNotVerifyException("please verify email");
        }
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

    @Override
    public TokenResponse refreshToken(HttpServletRequest req, HttpServletResponse res) throws DataNotFoundException {
        String refreshToken = cookieHandler.getCookie(req,"refresh_token");
        if(refreshToken == null) {
            cookieHandler.addCookie(res, "refresh_token", null, 0);
            throw new DataNotFoundException("refresh token not found");
        }
        Token token = tokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new DataNotFoundException("refreshToken is not exist"));
        String username = jwtService.extractUsername(refreshToken);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new DataNotFoundException("user not found"));
        UserDetail userDetail = new UserDetail(user);
        if(!jwtService.validateRefreshToken(refreshToken, userDetail)) {
            tokenRepository.delete(token);
            cookieHandler.addCookie(res, "refresh_token", null, 0);
            throw new DataNotFoundException("refresh token is incorrect");
        }
        token.setAccessToken(jwtService.generateToken(userDetail));
        tokenRepository.save(token);
        return TokenResponse.builder()
                .accessToken(jwtService.generateToken(userDetail))
                .refreshToken(jwtService.generateRefreshToken(new HashMap<>(), userDetail))
                .build();
    }

    @Override
    public User checkLogin(HttpServletRequest req, HttpServletResponse res) {
        String refreshToken = cookieHandler.getCookie(req, "refresh_token");
        if(refreshToken == null) return null;
        Token token = tokenRepository.findByRefreshToken(refreshToken)
                .orElse(null);
        if(token == null) return null;
        String username = jwtService.extractUsername(refreshToken);
        if(username == null) return null;
        return userRepository.findByUsername(username)
                .orElse(null);
    }

    @Override
    public void logout(HttpServletRequest req, HttpServletResponse res) {
        String refreshToken = cookieHandler.getCookie(req, "refresh_token");
        if(refreshToken == null) return;
        Token token = tokenRepository.findByRefreshToken(refreshToken)
                .orElse(null);
        if(token == null) return;
        tokenRepository.delete(token);
        cookieHandler.addCookie(res, "refresh_token", null, 0);
        cookieHandler.addCookie(res, "access_token", null, 0);
    }

    @Override
    public void resetPassword(String email) throws DataNotFoundException, MessagingException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new DataNotFoundException("user not found"));
        user.setVerifiedResetPassword(false);
        user.setOtp(generateOtp());
        user.setExpiryDateOtp(LocalDateTime.now().plusMinutes(5));
        sendMail(user);
        userRepository.save(user);
    }

    @Override
    public void verifyEmailResetPassword(String email, String otp) throws DataNotFoundException, DataNotMatchedException, DataExpiredException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new DataNotFoundException("email not found"));
        if(!user.getOtp().equals(otp))
            throw new DataNotMatchedException("otp không chính xác");
        if(user.getExpiryDateOtp().isBefore(LocalDateTime.now()))
            throw new DataExpiredException("otp đã hết hạn");
        user.setVerifiedResetPassword(true);
        userRepository.save(user);
    }

    @Override
    public TokenResponse createNewPassword(CreateNewPasswordRequest createNewPasswordRequest) throws DataNotMatchedException, DataNotFoundException, UserNotVerifyException {
        User user = userRepository.findByEmail(createNewPasswordRequest.getEmail())
                .orElseThrow(() -> new DataNotFoundException("user not found"));
        if(!createNewPasswordRequest.getPassword()
                .equals(createNewPasswordRequest.getConfirmPassword())) throw new DataNotMatchedException("confirm password is incorrect");
        if(!user.getVerifiedResetPassword())
            throw new UserNotVerifyException("unverified email");

        user.setPassword(passwordEncoder.encode(createNewPasswordRequest.getPassword()));
        user.setVerifiedResetPassword(false);
        userRepository.save(user);
        deleteAllToken(user.getId());
        return saveToken(user);
    }

    @Override
    public TokenResponse changePassword(ChangePasswordRequest changePasswordRequest) throws DataNotFoundException, DataNotMatchedException {
        User user = userRepository.findByEmail(changePasswordRequest.getEmail())
                .orElseThrow(() -> new DataNotFoundException("user not found"));
        if(!passwordEncoder.matches(changePasswordRequest.getOldPassword(), user.getPassword()))
            throw new DataNotMatchedException("old password is incorrect");
        user.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
        userRepository.save(user);
        deleteAllToken(user.getId());
        return saveToken(user);
    }

    private TokenResponse saveToken(User user) {
        List<Token> tokens = tokenRepository.findAllByUserId(user.getId());
        if(!tokens.isEmpty() && tokens.size() >= 2) {
            Token tokenDelete = tokens.get(tokens.size() - 1);
            tokenRepository.delete(tokenDelete);
        }
        UserDetail userDetail = new UserDetail(user);
        Token token = Token.builder()
                .accessToken(jwtService.generateToken(userDetail))
                .refreshToken(jwtService.generateRefreshToken(new HashMap<>(), userDetail))
                .userId(user.getId())
                .expiredDate(LocalDateTime.now().plusDays(30))
                .build();
        tokenRepository.save(token);
        return TokenResponse.builder()
                .accessToken(jwtService.generateToken(userDetail))
                .refreshToken(jwtService.generateRefreshToken(new HashMap<>(), userDetail))
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

    private void deleteAllToken(String userId) {
        List<Token> tokens = tokenRepository.findAllByUserId(userId);
        tokenRepository.deleteAll(tokens);
    }


    private String generateOtp() {
        Random random = new Random();
        StringBuilder otp = new StringBuilder();
        for(int i = 0; i <= 5; i++) {
            otp.append(random.nextInt(10));
        }
        return otp.toString();
    }


}
