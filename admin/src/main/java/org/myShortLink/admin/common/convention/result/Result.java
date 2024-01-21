package org.myShortLink.admin.common.convention.result;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Result<T> implements Serializable {

    public static final String SUCCESS_CODE = "0";

    private String code;
    private  String message;
    private T data;
    private String requestId;

    public boolean isSuccess() {
        return SUCCESS_CODE.equals(code);
    }
}
