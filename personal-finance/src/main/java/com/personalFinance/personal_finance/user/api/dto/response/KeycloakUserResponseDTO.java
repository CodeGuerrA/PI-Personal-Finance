package com.personalFinance.personal_finance.user.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class KeycloakUserResponseDTO {
    private String userId;
    private String temporaryPassword;
}
