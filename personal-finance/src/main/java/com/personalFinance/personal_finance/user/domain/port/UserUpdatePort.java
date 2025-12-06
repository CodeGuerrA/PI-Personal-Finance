package com.personalFinance.personal_finance.user.domain.port;

import com.personalFinance.personal_finance.user.domain.entity.User;

public interface UserUpdatePort {
    User update(User user);
}
