package com.celica.infinity.common.auth.services;

import com.celica.infinity.common.auth.enums.Context;
import com.celica.infinity.common.auth.models.Code;
import com.celica.infinity.common.auth.models.User;
import com.celica.infinity.common.auth.repositories.CodeRepository;
import com.celica.infinity.utils.Utils;
import com.celica.infinity.utils.notification.EmailService;
import com.celica.infinity.utils.notification.SmsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class CodeService {

    private final Logger logger = LoggerFactory.getLogger(AuthService.class);
    private final CodeRepository codeRepository;
    private final EmailService emailService;
    private final SmsService smsService;

    public CodeService(
            CodeRepository codeRepository,
            EmailService emailService,
            SmsService smsService
    ){
        this.codeRepository = codeRepository;
        this.emailService = emailService;
        this.smsService = smsService;
    }

    public Code generateCode(User user, Context context) {
        Code code = Code.builder()
                .code(Utils.generateRandomAlphanumericString(6))
                .user(user)
                .context(context)
                .expiry(LocalDateTime.now().plusMinutes(5))
                .build();
        codeRepository.save(code);
        return code;
    }

    public Code generateCode(User user, Context context, int expiry) {
        Code code = Code.builder()
                .code(Utils.generateRandomAlphanumericString(6))
                .user(user)
                .context(context)
                .expiry(LocalDateTime.now().plusMinutes(expiry))
                .build();
        codeRepository.save(code);
        return code;
    }

    public Optional<Code> getCode(User user, String code, Context context) {
        return codeRepository.findFirstByUserAndCodeAndUsedAndContextOrderByCreatedAtDesc(user, code, false,
                context);
    }

    @Async
    protected void sendCode(User user, Context context) {
        Code code = this.generateCode(user, context);
        if (!Utils.isStringNullOrEmpty(user.getEmail())){
            sendCodeEmail(user, context, code);
        }
        if (!Utils.isStringNullOrEmpty(user.getPhoneNumber())){
            sendCodeSms(user, context, code);
        }
    }

    public void sendCodeEmail(User user, Context context, Code code){
        String message = "Hi "+user.getFullName()+",\nYour account "+context.toString().toLowerCase()+" code is "
                +code.getCode();
        String subject = context.toString().toLowerCase()+" Code";
        emailService.sendEmail(user.getEmail(), subject, message);
    }

    public void sendCodeSms(User user, Context context, Code code) {
        try {
            String message = context.toString().toLowerCase()+" code is "+code.getCode();
            smsService.sendSms(message, user.getPhoneNumber());
        } catch (Exception e) {
            logger.error("Error sending message", e);
        }
    }
}
