package com.personalFinance.personal_finance.user.application.facade;

import com.personalFinance.personal_finance.user.api.dto.request.ChangePasswordRequestDTO;
import com.personalFinance.personal_finance.user.api.dto.request.UserCreateRequestDTO;
import com.personalFinance.personal_finance.user.api.dto.request.UserSetPasswordRequestDTO;
import com.personalFinance.personal_finance.user.api.dto.request.UserUpdateRequestDTO;
import com.personalFinance.personal_finance.user.api.dto.response.UserResponseDTO;

import java.util.List;


public interface UserService {
    void createUser(UserCreateRequestDTO dto);

    void setPermanentPassword(Long id, UserSetPasswordRequestDTO userSetPasswordRequestDTO);

    void changePassword(String keycloakId, ChangePasswordRequestDTO dto);

    List<UserResponseDTO> findAllUsers();

    UserResponseDTO findUserByKeycloakId(String keycloakId);

    void updateUser(String keycloakId, UserUpdateRequestDTO dto);

    void deleteUser(String keycloakId);

}
