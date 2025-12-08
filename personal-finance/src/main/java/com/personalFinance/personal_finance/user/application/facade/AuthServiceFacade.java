package com.personalFinance.personal_finance.user.application.facade;

import com.personalFinance.personal_finance.user.api.dto.request.FirstAccessRequestDTO;
import com.personalFinance.personal_finance.user.api.dto.request.ForgotPasswordRequestDTO;
import com.personalFinance.personal_finance.user.api.dto.request.ResetPasswordWithCodeRequestDTO;
import com.personalFinance.personal_finance.user.api.dto.request.UserLoginRequestDTO;
import com.personalFinance.personal_finance.user.api.dto.response.UserLoginResponseDTO;
import com.personalFinance.personal_finance.user.application.auth.UserAuth;
import com.personalFinance.personal_finance.user.application.auth.UserFirstAccess;
import com.personalFinance.personal_finance.user.application.auth.UserRefreshToken;
import com.personalFinance.personal_finance.user.application.service.PasswordRecoveryRequestService;
import com.personalFinance.personal_finance.user.application.service.PasswordResetService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceFacade implements AuthService {
    private final UserAuth userAuth;
    private final UserRefreshToken userRefreshToken;
    private final UserFirstAccess userFirstAccess;
    private final PasswordRecoveryRequestService passwordRecoveryRequestService;
    private final PasswordResetService passwordResetService;

    @Override
    public UserLoginResponseDTO login(UserLoginRequestDTO userLoginRequestDTO) {
        return userAuth.loginUser(userLoginRequestDTO);
    }

    @Override
    public UserLoginResponseDTO token(String refreshToken) {
        return userRefreshToken.refreshToken(refreshToken);
    }

    @Override
    public void requestPasswordRecovery(ForgotPasswordRequestDTO dto) {
        passwordRecoveryRequestService.requestPasswordRecovery(dto);
    }

    @Override
    public void resetPasswordWithCode(ResetPasswordWithCodeRequestDTO dto) {
        passwordResetService.resetPasswordWithCode(dto);
    }

    @Override
    public void handleFirstAccess(FirstAccessRequestDTO dto) {
        userFirstAccess.handleFirstAccess(dto);
    }
}
