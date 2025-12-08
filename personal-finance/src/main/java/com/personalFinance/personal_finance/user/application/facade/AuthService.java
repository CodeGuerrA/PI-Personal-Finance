package com.personalFinance.personal_finance.user.application.facade;

import com.personalFinance.personal_finance.user.api.dto.request.FirstAccessRequestDTO;
import com.personalFinance.personal_finance.user.api.dto.request.ForgotPasswordRequestDTO;
import com.personalFinance.personal_finance.user.api.dto.request.ResetPasswordWithCodeRequestDTO;
import com.personalFinance.personal_finance.user.api.dto.request.UserLoginRequestDTO;
import com.personalFinance.personal_finance.user.api.dto.response.UserLoginResponseDTO;

public interface AuthService {
    UserLoginResponseDTO login(UserLoginRequestDTO userLoginRequestDTO);

    UserLoginResponseDTO token(String refreshToken);

    void requestPasswordRecovery(ForgotPasswordRequestDTO dto);

    void resetPasswordWithCode(ResetPasswordWithCodeRequestDTO dto);

    void handleFirstAccess(FirstAccessRequestDTO dto);
}
