package me.apella.booklib.handler;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum BusinessErrorCodes {
    N0_CODE(0, "No implementation found.", HttpStatus.NOT_IMPLEMENTED),
    INCORRECT_CURRENT_PASSWORD(300, "Current password is incorrect", HttpStatus.BAD_REQUEST),
    NEW_PASSWORD_DOES_NOT_MATCH(301, "New password does not match.", HttpStatus.BAD_REQUEST),
    ACCOUNT_LOCKED(302, "User account locked.", HttpStatus.FORBIDDEN),
    ACCOUNT_DISABLED(303, "User account is disabled.", HttpStatus.FORBIDDEN),
    INVALID_CREDENTIALS(304, "Invalid credentials", HttpStatus.FORBIDDEN);

    private final int code;
    private final String description;
    private final HttpStatus httpStatusCode;

    BusinessErrorCodes(int code, String description, HttpStatus httpStatusCode) {
        this.code = code;
        this.description = description;
        this.httpStatusCode = httpStatusCode;
    }
}
