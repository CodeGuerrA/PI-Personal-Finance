package com.personalFinance.personal_finance.user.domain.entity;

import com.personalFinance.personal_finance.user.domain.exception.InvalidCpfException;
import com.personalFinance.personal_finance.user.domain.exception.InvalidEmailException;
import jakarta.persistence.*;
import lombok.*;

import java.util.regex.Pattern;

/**
 * Rich Domain Model: Entidade com comportamento e validações internas.
 * Segue princípios de DDD (Domain-Driven Design).
 */
@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)// onlyExplicitlyIncluded = true evita problemas com lazy loading
@ToString(of = {"id", "userName", "email", "cpf"})
public class User {

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );

    private static final Pattern CPF_PATTERN = Pattern.compile("^\\d{11}$");

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "user_name", nullable = false, unique = true, length = 100)
    private String userName;

    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @Column(name = "email", nullable = false, unique = true, length = 150)
    private String email;

    @Column(name = "cpf", nullable = false, unique = true, length = 11)
    private String cpf;

    @Column(name = "keycloak_id", unique = true, length = 100)
    private String keycloakId;

    /**
     * Factory method para criar novo usuário com validações.
     */
    public static User create(String userName, String firstName, String lastName, String email, String cpf, String keycloakId) {
        validateUserName(userName);
        validateFirstName(firstName);
        validateLastName(lastName);
        validateEmail(email);
        validateCpf(cpf);
        validateKeycloakId(keycloakId);

        return User.builder()
                .userName(userName)
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .cpf(cpf)
                .keycloakId(keycloakId)
                .build();
    }

    /**
     * Atualiza o email do usuário com validação.
     */
    public void updateEmail(String newEmail) {
        validateEmail(newEmail);
        this.email = newEmail;
    }

    // Validações privadas
    private static void validateUserName(String userName) {
        if (userName == null || userName.isBlank()) {
            throw new IllegalArgumentException("Username não pode ser nulo ou vazio");
        }
        if (userName.length() > 100) {
            throw new IllegalArgumentException("Username não pode ter mais de 100 caracteres");
        }
    }

    private static void validateFirstName(String firstName) {
        if (firstName == null || firstName.isBlank()) {
            throw new IllegalArgumentException("Primeiro nome não pode ser nulo ou vazio");
        }
        if (firstName.length() > 100) {
            throw new IllegalArgumentException("Primeiro nome não pode ter mais de 100 caracteres");
        }
    }

    private static void validateLastName(String lastName) {
        if (lastName == null || lastName.isBlank()) {
            throw new IllegalArgumentException("Sobrenome não pode ser nulo ou vazio");
        }
        if (lastName.length() > 100) {
            throw new IllegalArgumentException("Sobrenome não pode ter mais de 100 caracteres");
        }
    }

    private static void validateEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new InvalidEmailException("Email não pode ser nulo ou vazio");
        }
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new InvalidEmailException(email);
        }
        if (email.length() > 150) {
            throw new InvalidEmailException("Email não pode ter mais de 150 caracteres");
        }
    }

    private static void validateKeycloakId(String keycloakId) {
        if (keycloakId == null || keycloakId.isBlank()) {
            throw new IllegalArgumentException("Keycloak ID não pode ser nulo ou vazio");
        }
    }

    private static void validateCpf(String cpf) {
        if (cpf == null || cpf.isBlank()) {
            throw new InvalidCpfException("CPF não pode ser nulo ou vazio", cpf);
        }

        // Remove caracteres não numéricos
        String cleanCpf = cpf.replaceAll("[^0-9]", "");

        if (!CPF_PATTERN.matcher(cleanCpf).matches()) {
            throw new InvalidCpfException("CPF deve conter 11 dígitos", cpf);
        }

        // Valida se todos os dígitos são iguais (CPFs inválidos como 111.111.111-11)
        if (cleanCpf.matches("(\\d)\\1{10}")) {
            throw new InvalidCpfException(cpf);
        }

        // Validação dos dígitos verificadores
        if (!isValidCpfCheckDigits(cleanCpf)) {
            throw new InvalidCpfException(cpf);
        }
    }

    private static boolean isValidCpfCheckDigits(String cpf) {
        // Calcula primeiro dígito verificador
        int sum = 0;
        for (int i = 0; i < 9; i++) {
            sum += Character.getNumericValue(cpf.charAt(i)) * (10 - i);
        }
        int firstDigit = 11 - (sum % 11);
        if (firstDigit >= 10) firstDigit = 0;

        // Calcula segundo dígito verificador
        sum = 0;
        for (int i = 0; i < 10; i++) {
            sum += Character.getNumericValue(cpf.charAt(i)) * (11 - i);
        }
        int secondDigit = 11 - (sum % 11);
        if (secondDigit >= 10) secondDigit = 0;

        // Verifica se os dígitos calculados conferem
        return firstDigit == Character.getNumericValue(cpf.charAt(9)) &&
               secondDigit == Character.getNumericValue(cpf.charAt(10));
    }
}
