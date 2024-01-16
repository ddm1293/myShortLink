package org.myShortLink.admin.common.convention.exception;

import lombok.Data;
import org.myShortLink.admin.common.convention.error.IErrorCode;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Data
public abstract class AbstractException extends RuntimeException {

    public final String errorCode;
    public final String errorMessage;

    public AbstractException(String message, Throwable throwable, IErrorCode errorCode) {
        super(message, throwable);
        this.errorCode = errorCode.code();
        this.errorMessage = Optional.ofNullable(StringUtils.hasLength(message) ? message : null).orElse(errorCode.message());
    }
}
