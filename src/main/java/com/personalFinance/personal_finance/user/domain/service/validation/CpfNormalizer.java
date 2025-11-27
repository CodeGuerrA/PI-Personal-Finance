package com.personalFinance.personal_finance.user.domain.service.validation;

import org.springframework.stereotype.Component;

@Component
public class CpfNormalizer {
    public String normalize(String cpf) {
        if (cpf == null) {
            return null;
        }
        return cpf.replaceAll("[^0-9]", "");
    }
}
