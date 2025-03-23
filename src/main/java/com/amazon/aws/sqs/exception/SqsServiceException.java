package com.amazon.aws.sqs.exception;

public class SqsServiceException extends RuntimeException {
    private final String msgCode;
    private final String exceptionMessage;

    public SqsServiceException(String msgCode, String exceptionMessage) {
        super(exceptionMessage);
        this.msgCode = msgCode;
        this.exceptionMessage = exceptionMessage;
    }

    public String getMsgCode() {
        return msgCode;
    }

    public String getExceptionMessage() {
        return exceptionMessage;
    }
}
