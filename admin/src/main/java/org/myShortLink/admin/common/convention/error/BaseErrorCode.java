package org.myShortLink.admin.common.convention.error;

public enum BaseErrorCode implements IErrorCode {

    // Tier-One Error: Client Error
    CLIENT_ERROR("A000001", "Client Side Error"),

    // Tier-Two Errors: User Registration Error
    USER_REGISTER_ERROR("A000100", "User Registration Error"),
    USER_NAME_VERIFY_ERROR("A000110", "Username Verification Failed"),
    USER_NAME_EXIST_ERROR("A000111", "Username Already Exists"),
    USER_NAME_SENSITIVE_ERROR("A000112", "Username Contains Sensitive Words"),
    USER_NAME_SPECIAL_CHARACTER_ERROR("A000113", "Username Contains Special Characters"),
    USER_SAVE_ERROR("A000114", "Error Saving User"),
    USER_REGISTRATION_BUSY("A000115", "Multiple User Registration, Busy"),
    USER_REGISTRATION_INTERRUPTED("A000116", "User Registration Interrupted"),
    USER_EMAIL_EXIST_ERROR("A000117", "Username Email Exists"),
    USER_PHONE_NUMBER_EXIST_ERROR("A000118", "Username Phone Number Exists"),
    PASSWORD_VERIFY_ERROR("A000120", "Password Verification Failed"),
    PASSWORD_SHORT_ERROR("A000121", "Password Length Insufficient"),
    USER_JSON_PARSE_ERROR("A000122", "Cannot Parse User JSON"),
    PASSWORD_MISMATCH_ERROR("A000123", "Password Mismatch"),

    //  Tier-Two Errors: System Requests Missing Idempotent Token
    IDEMPOTENT_TOKEN_NULL_ERROR("A000200", "Idempotent Token is Null"),
    IDEMPOTENT_TOKEN_DELETE_ERROR("A000201", "Idempotent Token Has Been Used or Expired"),

    // Tier-One Error: System Execution Error
    SERVICE_ERROR("B000001", "System Execution Error"),

    // Tier-Two Errors: System Execution Error
    SERVICE_TIMEOUT_ERROR("B000100", "System Execution Timeout"),
    USER_NULL_ERROR("B000200", "Null User Found"),

    // Tier-One Error: Calling Third Party Services
    REMOTE_ERROR("C000001", "Error Calling Third Party Services");


    private final String code;
    private final String message;

    BaseErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String code() {
        return code;
    }

    @Override
    public String message() {
        return message;
    }
}
