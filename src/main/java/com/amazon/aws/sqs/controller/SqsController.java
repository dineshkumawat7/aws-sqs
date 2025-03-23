package com.amazon.aws.sqs.controller;

import com.amazon.aws.sqs.dto.Response;
import com.amazon.aws.sqs.exception.SqsServiceException;
import com.amazon.aws.sqs.service.SqsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import software.amazon.awssdk.services.sqs.model.QueueAttributeName;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/sqs")
public class SqsController {

    private static final Logger logger = LoggerFactory.getLogger(SqsController.class);

    @Autowired
    private SqsService sqsService;

    @PostMapping(value = "createQueue/{queueName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response<?>> createQueue(@PathVariable("queueName") String queueName) {
        Response<?> response = null;
        try {
            response = sqsService.createQueue(queueName);
        } catch (SqsServiceException e) {
            logger.error("Error occurred when creating sqs queue: {}.", e.getMessage());
            throw new SqsServiceException(e.getMsgCode(), e.getExceptionMessage());
        }
        logger.info("Sqs queue {} created successfully.", queueName);
        return ResponseEntity.status(Integer.parseInt(response.getStatusCode())).body(response);
    }

    @GetMapping(value = "listQueue", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response<List<String>>> listQueue() {
        Response<List<String>> response = null;
        try {
            response = sqsService.listQueue();
        } catch (SqsServiceException e) {
            logger.error("Error occurred when fetching sqs queue: {}.", e.getMessage());
            throw new SqsServiceException(e.getMsgCode(), e.getExceptionMessage());
        }
        logger.info(response.getMessage());
        return ResponseEntity.status(Integer.parseInt(response.getStatusCode())).body(response);
    }

    @GetMapping(value = "queueInfo", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response<Map<QueueAttributeName, String>>> getQueueInfo(@RequestParam(required = true) String queueName) {
        Response<Map<QueueAttributeName, String>> response = null;
        try {
            response = sqsService.queueInfo(queueName);
        } catch (SqsServiceException e) {
            logger.error("Error occurred when fetching sqs queue info: {}.", e.getMessage());
            throw new SqsServiceException(e.getMsgCode(), e.getExceptionMessage());
        }
        logger.info(response.getMessage());
        return ResponseEntity.status(Integer.parseInt(response.getStatusCode())).body(response);
    }

    @DeleteMapping(value = "deleteQueue", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response<?>> deleteQueue(@RequestParam(required = true) String queueName) {
        Response<?> response = null;
        try {
            response = sqsService.deleteQueue(queueName);
        } catch (SqsServiceException e) {
            logger.error("Error occurred when deleting sqs queue: {}.", e.getMessage());
            throw new SqsServiceException(e.getMsgCode(), e.getExceptionMessage());
        }
        logger.info(response.getMessage());
        return ResponseEntity.status(Integer.parseInt(response.getStatusCode())).body(response);
    }

    /**
     * This endpoint is consumed by load balancer for health check.
     *
     * @return Object
     */
    @GetMapping("/status")
    public Object healthCheck() {
        return "AWS SQS service is running up";
    }
}
