package com.co.ke.moneypal.moneypal.wrapper;

import lombok.Data;

@Data
public class ResetPasswordWrapper {
    private String username;
    private String newPassword;
    private String confirmPassword;
}
