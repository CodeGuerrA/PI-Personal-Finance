package com.personalFinance.personal_finance.user.api.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResetPasswordWithCodeRequestDTO {
    @NotBlank(message = "O email é obrigatório")
    @Email(message = "Email inválido")
    private String email;

    @NotBlank(message = "O código de verificação é obrigatório")
    @Pattern(regexp = "^[0-9]{6}$", message = "O código deve conter 6 dígitos")
    private String code;

    @NotBlank(message = "A nova senha é obrigatória")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "A senha deve ter no mínimo 8 caracteres, conter ao menos uma letra maiúscula, uma letra minúscula, um número e um caractere especial"
    )
    private String newPassword;
}
