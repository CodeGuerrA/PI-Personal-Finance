package com.personalFinance.personal_finance.user.api.mapper;

import com.personalFinance.personal_finance.user.api.dto.response.UserResponseDTO;
import com.personalFinance.personal_finance.user.domain.entity.User;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * Mapper para conversão entre User (entidade de domínio) e DTOs de resposta.
 * Nota: Não converte DTOs para User, pois agora usamos User.create() (Rich Domain Model).
 */
@Mapper(componentModel = "spring")
public interface UserMapper {

    /**
     * Converte User entity para DTO de resposta.
     */
    UserResponseDTO toUserResponseDTO(User user);

    /**
     * Converte lista de Users para lista de DTOs.
     */
    List<UserResponseDTO> toUserResponseDTOList(List<User> users);
}
