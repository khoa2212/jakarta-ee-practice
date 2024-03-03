package com.axonactive.dojo.auth.message;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthMessage {
    public static final String NOT_FOUND_USER = "This user is not existed";

    public static final String EXISTED_EMAIL = "This user's email is already existed";

    public static final String PASSWORDS_MUST_MATCH = "Passwords must be matched";

    public static final String PASSWORD_OR_EMAIL_IS_NOT_CORRECT = "Password or email is not correct";
}
