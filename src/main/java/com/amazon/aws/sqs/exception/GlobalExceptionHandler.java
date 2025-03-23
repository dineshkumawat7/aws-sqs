package com.amazon.aws.sqs.exception;

import com.amazon.aws.sqs.dto.ErrorResponse;
import com.amazon.aws.sqs.utils.Constants;
import com.amazon.aws.sqs.utils.Utility;
import org.apache.tomcat.util.bcel.Const;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(SqsServiceException.class)
    public ResponseEntity<ErrorResponse> sqsServiceExceptionHandler(SqsServiceException e) {
        ErrorResponse response = new ErrorResponse();
        response.setTimestamp(Utility.getCurrentDate());
        response.setStatus(Constants.FAILURE_TAG);
        response.setErrorCode(e.getMsgCode());
        response.setErrorMessage(e.getExceptionMessage());
        return ResponseEntity.status(Integer.parseInt(e.getMsgCode())).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> exceptionHandler(Exception e) {
        ErrorResponse response = new ErrorResponse();
        response.setTimestamp(Utility.getCurrentDate());
        response.setStatus(Constants.FAILURE_TAG);
        response.setErrorCode("500");
        response.setErrorMessage(e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
