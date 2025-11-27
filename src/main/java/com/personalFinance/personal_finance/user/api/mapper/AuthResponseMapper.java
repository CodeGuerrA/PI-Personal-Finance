package com.personalFinance.personal_finance.user.api.mapper;

import com.personalFinance.personal_finance.user.api.dto.response.UserLoginResponseDTO;
import org.keycloak.representations.AccessTokenResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AuthResponseMapper {

    @Mapping(target = "accessToken", source = "token")
    @Mapping(target = "refreshToken", source = "refreshToken")
    @Mapping(target = "expiresIn", source = "expiresIn")
    @Mapping(target = "refreshExpiresIn", source = "refreshExpiresIn")
    UserLoginResponseDTO toUserLoginResponseDTO(AccessTokenResponse tokenResponse);
}
