package com.amazon.aws.sqs.controller;

import com.amazon.aws.sqs.dto.Response;
import com.amazon.aws.sqs.exception.SqsServiceException;
import com.amazon.aws.sqs.service.SqsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Operation(summary = "New queue created", description = "This api is used to create new aws sqs queue")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Queue created successfully",
                    content = @Content(schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Resource not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping(value = "createQueue", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response<Map<QueueAttributeName, String>>> createQueue(@RequestParam(required = true) String queueName) {
        Response<Map<QueueAttributeName, String>> response = null;
        try {
            response = sqsService.createQueue(queueName);
        } catch (SqsServiceException e) {
            logger.error("Error occurred when creating sqs queue: {}.", e.getMessage());
            throw new SqsServiceException(e.getMsgCode(), e.getExceptionMessage());
        }
        logger.info("Sqs queue created successfully: {}", queueName);
        return ResponseEntity.status(Integer.parseInt(response.getStatusCode())).body(response);
    }

    @Operation(summary = "Fetching all queue list", description = "This api is used to get aws sqs queue list")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Fetching all queue list successfully",
                    content = @Content(schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Resource not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
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

    @Operation(summary = "Fetching queue information", description = "This api is used to get aws sqs queue information.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Fetching queue information successfully",
                    content = @Content(schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Resource not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
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

    @Operation(summary = "Delete queue", description = "This api is used to delete aws sqs queue.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "SQS queue deleted successfully",
                    content = @Content(schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Resource not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
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
    @Operation(summary = "Health Check", description = "Checks the status of the application.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Application is running"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/status")
    public Object healthCheck() {
        return "AWS SQS service is running up";
    }
}
