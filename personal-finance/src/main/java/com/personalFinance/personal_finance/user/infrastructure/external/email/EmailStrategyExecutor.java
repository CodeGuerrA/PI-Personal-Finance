package com.personalFinance.personal_finance.user.infrastructure.external.email;

import com.personalFinance.personal_finance.user.infrastructure.external.email.strategy.UserNotificationStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class EmailStrategyExecutor {
    private final EmailSenderService emailSenderService;

    public <T> void execute(String to, UserNotificationStrategy<T> strategy, T dto) {
        log.info("Enviando email para: {}", to);
        String subject = strategy.getSubject();
        String htmlContent = strategy.buildContent(dto);
        emailSenderService.sendHtmlEmail(to, subject, htmlContent);
        log.info("Email enviado com sucesso para: {}", to);
    }
}
