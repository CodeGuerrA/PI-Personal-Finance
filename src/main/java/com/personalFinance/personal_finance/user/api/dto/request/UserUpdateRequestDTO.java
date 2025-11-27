package com.personalFinance.personal_finance.user.api.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateRequestDTO {
    @Email(message = "Email inválido, exemplo: usuario@dominio.com")
    @NotBlank(message = "Email é obrigatório")
    @Size(max = 100, message = "Email não pode ter mais de 100 caracteres")
    private String email;
}
