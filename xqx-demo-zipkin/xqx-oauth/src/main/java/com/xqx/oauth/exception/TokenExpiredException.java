package com.xqx.oauth.exception;

import com.xqx.base.exception.ErrorCode;

/**
 * Token过期异常
 */
public class TokenExpiredException extends Exception{

    private ErrorCode errorCode;
    private String errMsg;

    public TokenExpiredException(ErrorCode errorCode, String errMsg){
        super(errMsg);
        this.errorCode = errorCode;
        this.errMsg = errMsg;
    }

    public TokenExpiredException(Throwable throwable, ErrorCode errorCode, String errMsg) {
        super(throwable.getMessage(), throwable);
        this.errorCode = errorCode;
        this.errMsg = errMsg;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

}
