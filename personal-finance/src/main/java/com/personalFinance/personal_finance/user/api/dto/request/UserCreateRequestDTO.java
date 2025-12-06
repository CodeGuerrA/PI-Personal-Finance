package com.personalFinance.personal_finance.user.api.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.br.CPF;

@Getter
@Setter
public class UserCreateRequestDTO {

    @NotBlank(message = "Nome não pode ficar vazio")
    @Size(max = 50, message = "Não pode ter mais de 50 caracteres")
    private String firstName;

    @NotBlank(message = "Sobrenome não pode ficar vazio")
    @Size(max = 50, message = "Não pode ter mais de 50 caracteres")
    private String lastName;

    @Email(message = "Email inválido, exemplo: usuario@dominio.com")
    @NotBlank(message = "Email é obrigatório")
    @Size(max = 100, message = "Email não pode ter mais de 100 caracteres")
    private String email;

    @NotBlank(message = "CPF é obrigatório")
    @CPF(message = "CPF inválido")
    private String cpf;
}
