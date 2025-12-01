package com.personalFinance.personal_finance.user.application.facade;

import com.personalFinance.personal_finance.user.api.dto.request.ChangePasswordRequestDTO;
import com.personalFinance.personal_finance.user.api.dto.request.UserCreateRequestDTO;
import com.personalFinance.personal_finance.user.api.dto.request.UserSetPasswordRequestDTO;
import com.personalFinance.personal_finance.user.api.dto.request.UserUpdateRequestDTO;
import com.personalFinance.personal_finance.user.api.dto.response.UserResponseDTO;
import com.personalFinance.personal_finance.user.application.auth.UserPasswordManager;
import com.personalFinance.personal_finance.user.application.service.UserCreator;
import com.personalFinance.personal_finance.user.application.service.UserDeleter;
import com.personalFinance.personal_finance.user.application.service.UserFinder;
import com.personalFinance.personal_finance.user.application.service.UserUpdater;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceFacade implements UserService {
    private final UserCreator userCreator;
    private final UserPasswordManager userPasswordManager;
    private final UserFinder userFinder;
    private final UserUpdater userUpdater;
    private final UserDeleter userDeleter;

    @Override
    public void createUser(UserCreateRequestDTO dto) {
        userCreator.createUser(dto);
    }

    @Override
    public void setPermanentPassword(Long id, UserSetPasswordRequestDTO userSetPasswordRequestDTO) {
        userPasswordManager.setPermanentPassword(id, userSetPasswordRequestDTO);
    }

    @Override
    public void changePassword(String keycloakId, ChangePasswordRequestDTO dto) {
        userPasswordManager.changePassword(keycloakId, dto);
    }

    @Override
    public List<UserResponseDTO> findAllUsers() {
        return userFinder.findAllUsers();
    }

    @Override
    public UserResponseDTO findUserByKeycloakId(String keycloakId) {
        return userFinder.findUserByKeycloakId(keycloakId);
    }

    @Override
    public void updateUser(String keycloakId, UserUpdateRequestDTO dto) {
        userUpdater.updateUser(keycloakId, dto);
    }

    @Override
    public void deleteUser(String keycloakId) {
        userDeleter.deleteUser(keycloakId);
    }

}
