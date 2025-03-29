package com.amazon.aws.sqs.controller;

import com.amazon.aws.sqs.dto.Response;
import com.amazon.aws.sqs.exception.SqsServiceException;
import com.amazon.aws.sqs.service.SqsMessageService;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.util.List;

@RestController
@RequestMapping("/sqs/message")
public class SqsMessageController {

    private static final Logger logger = LoggerFactory.getLogger(SqsMessageController.class);
    @Autowired
    private SqsMessageService sqsMessageService;

    @Operation(summary = "Send message", description = "This api is used to sent message on aws sqs queue.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Message sent successfully",
                    content = @Content(schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Resource not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping(value = "/send", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response<Object>> sendMessage(@RequestParam("queueName") String queueName, @RequestBody Object object) {
        Response<Object> response = null;
        try {
            response = sqsMessageService.sendMessage(queueName, object);
        } catch (SqsServiceException e) {
            logger.error("Error occurred when sending message from sqs queue: {}.", e.getMessage());
            throw new SqsServiceException(e.getMsgCode(), e.getExceptionMessage());
        }
        logger.info(response.getMessage());
        return ResponseEntity.status(Integer.parseInt(response.getStatusCode())).body(response);
    }


    @Operation(summary = "Retrieve message", description = "This api is used to retrieve messages from aws sqs queue.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Message receive successfully",
                    content = @Content(schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "404", description = "Resource not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping(value = "/receive", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response<List<Object>>> receiveMessage(@RequestParam("queueName") String queueName) {
        Response<List<Object>> response = null;
        try {
            response = sqsMessageService.receiveMessage(queueName);
        } catch (SqsServiceException e) {
            logger.error("Error occurred when receive message from sqs queue: {}.", e.getMessage());
            throw new SqsServiceException(e.getMsgCode(), e.getExceptionMessage());
        }
        logger.info(response.getMessage());
        return ResponseEntity.status(Integer.parseInt(response.getStatusCode())).body(response);
    }
}
