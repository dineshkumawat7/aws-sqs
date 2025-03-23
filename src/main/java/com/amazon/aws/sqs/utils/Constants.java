package com.amazon.aws.sqs.utils;

public class Constants {
    public static final String SUCCESS_TAG = "Success";
    public static final String FAILURE_TAG = "Failure";
    public static final String OK_STATUS_CODE = "200";
    public static final String CREATED_STATUS_CODE = "201";
    public static final String BAD_REQUEST_STATUS_CODE = "400";
    public static final String NOT_FOUND_STATUS_CODE = "404";
    public static final String REQUEST_TIMEOUT_STATUS_CODE = "408";
    public static final String INTERNAL_SERVER_ERROR_STATUS_CODE = "500";
    public static final String RESPONSE_TAG = "response";
    public static final String METADATA_REGEX = "^[\\sa-zA-Z0-9.-]*$";
    public static final String NUMERIC_REGEX = "^[0-9.]*$";
}
