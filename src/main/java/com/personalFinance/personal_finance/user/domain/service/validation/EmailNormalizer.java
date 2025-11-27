package com.personalFinance.personal_finance.user.domain.service.validation;

import org.springframework.stereotype.Component;
//lembrar de corrigir os validadores e normalizer
@Component
public class EmailNormalizer {
    public String normalize(String email) {
        if (email == null || email.isBlank()) {
            return null;
        }
        return email.toLowerCase().trim();
    }
}
