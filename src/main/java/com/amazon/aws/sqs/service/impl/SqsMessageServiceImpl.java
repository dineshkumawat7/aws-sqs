package com.amazon.aws.sqs.service.impl;

import com.amazon.aws.sqs.dto.Response;
import com.amazon.aws.sqs.service.SqsMessageService;
import com.amazon.aws.sqs.utils.Constants;
import com.amazon.aws.sqs.utils.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.*;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SqsMessageServiceImpl implements SqsMessageService {

    @Autowired
    private SqsClient sqsClient;

    @Override
    public Response<Object> sendMessage(String queueName, Object object) {
        GetQueueUrlRequest queueUrlRequest = GetQueueUrlRequest.builder()
                .queueName(queueName)
                .build();
        GetQueueUrlResponse queueUrlResponse = sqsClient.getQueueUrl(queueUrlRequest);
        String queueUrl = queueUrlResponse.queueUrl();

        SendMessageRequest sendMessageRequest = SendMessageRequest.builder()
                .queueUrl(queueUrl)
                .messageBody(Utility.objectToJsonString(object))
                .delaySeconds(0)
                .build();
        SendMessageResponse sendMessageResponse = sqsClient.sendMessage(sendMessageRequest);
        String msg = String.format("Message sent successfully! Message ID: %s", sendMessageResponse.messageId());
        return new Response<>(Utility.getCurrentDate(), Constants.OK_STATUS_CODE, Constants.SUCCESS_TAG, msg, object);
    }

    @Override
    public Response<List<Object>> receiveMessage(String queueName) {
        GetQueueUrlRequest queueUrlRequest = GetQueueUrlRequest.builder()
                .queueName(queueName)
                .build();
        GetQueueUrlResponse queueUrlResponse = sqsClient.getQueueUrl(queueUrlRequest);
        String queueUrl = queueUrlResponse.queueUrl();
        ReceiveMessageRequest receiveMessageRequest = ReceiveMessageRequest.builder()
                .queueUrl(queueUrl)
                .maxNumberOfMessages(5)
                .waitTimeSeconds(10)
                .build();
        ReceiveMessageResponse receiveMessageResponse = sqsClient.receiveMessage(receiveMessageRequest);
        List<Object> messages = receiveMessageResponse.messages().stream().map(Message::body).collect(Collectors.toList());
        String msg = "Message receive successfully.";
        return new Response<>(Utility.getCurrentDate(), Constants.OK_STATUS_CODE, Constants.SUCCESS_TAG, msg, messages);
    }
}
