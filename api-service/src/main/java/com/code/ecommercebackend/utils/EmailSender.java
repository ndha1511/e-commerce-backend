package com.code.ecommercebackend.utils;


import com.code.ecommercebackend.models.Order;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class EmailSender {
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;
    @Value("${spring.mail.username}")
    private String sender;

    public String sendSimpleMail(EmailDetails details) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(sender);
        message.setTo(details.getRecipient());
        message.setSubject(details.getSubject());
        message.setText(details.getMsgBody());
        mailSender.send(message);
        return "mail send successfully";
    }

    public String sendMailWithAttachment(EmailDetails details) throws MessagingException, IOException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
        mimeMessageHelper.setFrom(sender);
        mimeMessageHelper.setTo(details.getRecipient());
        mimeMessageHelper.setSubject(details.getSubject());
        mimeMessageHelper.setText(details.getMsgBody(), true);
        MultipartFile file = details.getAttachment();
        Path tempFile = Files.createTempFile("temp", file.getOriginalFilename());
        Files.copy(file.getInputStream(), tempFile, StandardCopyOption.REPLACE_EXISTING);
        FileSystemResource fileSystemResource = new FileSystemResource(
                new File(tempFile.toFile().getAbsolutePath())
        );
        mimeMessageHelper.addAttachment(Objects.requireNonNull(file.getOriginalFilename()), fileSystemResource);
        mailSender.send(mimeMessage);
        return "mail send successfully";
    }


    public void sendHtmlMail(EmailDetails details) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
        mimeMessageHelper.setFrom(sender);
        mimeMessageHelper.setTo(details.getRecipient());
        mimeMessageHelper.setSubject(details.getSubject());
        Context context = new Context();
        Map<String, Object> variables = new HashMap<>();
        variables.put("msgBody", details.getMsgBody());
        context.setVariables(variables);
        String htmlContent = templateEngine.process("send-email", context);
        mimeMessageHelper.setText(htmlContent, true);
        mailSender.send(mimeMessage);
    }

    public void sendHtmlMailOrder(Order order, String emailRecipient, String subject) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
        mimeMessageHelper.setFrom(sender);
        mimeMessageHelper.setTo(emailRecipient);
        mimeMessageHelper.setSubject(subject);
        Context context = new Context();
        Map<String, Object> variables = new HashMap<>();
        variables.put("order", order);
        context.setVariables(variables);
        String htmlContent = templateEngine.process("order", context);
        mimeMessageHelper.setText(htmlContent, true);
        mailSender.send(mimeMessage);
    }
}
