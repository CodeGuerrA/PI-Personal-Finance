package com.personalFinance.personal_finance.user.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserLoginRequestDTO {
    @NotBlank(message = "Usuario não pode ser vazio")
    private String username;
    @NotBlank(message = "senha não pode ser vazia")
    private String password;
}
