package com.amazon.aws.sqs.utils;

import com.amazon.aws.sqs.exception.SqsServiceException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Component
public class Utility {

    public Utility() {
    }

    /**
     * This method is used to get current class name.
     *
     * @return Current class name
     */
    public static String getCurrentClassName() {
        return Thread.currentThread().getStackTrace()[2].getClassName();
    }

    /**
     * This method is used to get current method name.
     *
     * @return Current method name
     */
    public static String getCurrentMethodName() {
        return Thread.currentThread().getStackTrace()[2].getMethodName();
    }

    /**
     * This method is used to get current system datetime.
     *
     * @return Current datetime name
     */
    public static String getCurrentDate() {
        LocalDateTime localDateTime = LocalDateTime.now();
        OffsetDateTime offsetDateTime = localDateTime.atOffset(ZoneOffset.UTC);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        return offsetDateTime.format(formatter);
    }

    /**
     * This method is used to convert class object to JSON string.
     *
     * @param object - Object of a class
     * @return String - JSON string
     * @throws SqsServiceException - if exception occurred
     */
    public static String objectToJsonString(Object object) throws SqsServiceException {
        String jsonString = null;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
            objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
            jsonString = objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new SqsServiceException(Constants.INTERNAL_SERVER_ERROR_STATUS_CODE, e.getMessage());
        }
        return jsonString;
    }

    public static Map<String, Object> convertObjectToMap(Object object){
        return new ObjectMapper().convertValue(object, Map.class);
    }
}
