package com.personalFinance.personal_finance.user.domain.port;

import com.personalFinance.personal_finance.user.domain.entity.User;

public interface UserSavePort {
    User save(User user);
}
