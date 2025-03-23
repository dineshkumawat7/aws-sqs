package com.amazon.aws.sqs.controller;

import com.amazon.aws.sqs.dto.Response;
import com.amazon.aws.sqs.exception.SqsServiceException;
import com.amazon.aws.sqs.service.SqsMessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sqs/message")
public class SqsMessageController {

    private static final Logger logger = LoggerFactory.getLogger(SqsController.class);
    @Autowired
    private SqsMessageService sqsMessageService;

    @PostMapping(value = "/send", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response<Object>> sendMessage(@RequestParam("queueName") String queueName, @RequestBody Object object) {
        Response<Object> response = null;
        try {
            response = sqsMessageService.sendMessage(queueName, object);
        } catch (SqsServiceException e) {
            logger.error("Error occurred when sending message from sqs queue: {}.", e.getMessage());
            throw new SqsServiceException(e.getMsgCode(), e.getExceptionMessage());
        }
        return ResponseEntity.status(Integer.parseInt(response.getStatusCode())).body(response);
    }

    @PostMapping(value = "/receive", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response<List<Object>>> receiveMessage(@RequestParam("queueName") String queueName) {
        Response<List<Object>> response = null;
        try {
            response = sqsMessageService.receiveMessage(queueName);
        } catch (SqsServiceException e) {
            logger.error("Error occurred when receive message from sqs queue: {}.", e.getMessage());
            throw new SqsServiceException(e.getMsgCode(), e.getExceptionMessage());
        }
        return ResponseEntity.status(Integer.parseInt(response.getStatusCode())).body(response);
    }
}
