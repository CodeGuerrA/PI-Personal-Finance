package com.personalFinance.personal_finance.user.api.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponseDTO {
    private String id;
    private String userName;
    private String firstName;
    private String lastName;
    private String email;
}
