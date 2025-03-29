package com.amazon.aws.sqs.service.impl;

import com.amazon.aws.sqs.dto.Response;
import com.amazon.aws.sqs.exception.SqsServiceException;
import com.amazon.aws.sqs.service.SqsService;
import com.amazon.aws.sqs.utils.Constants;
import com.amazon.aws.sqs.utils.Utility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.*;

import java.util.List;
import java.util.Map;

@Service
public class SqsServiceImpl implements SqsService {

    private static final Logger logger = LoggerFactory.getLogger(SqsServiceImpl.class);

    @Autowired
    private SqsClient sqsClient;

    @Override
    public Response<Map<QueueAttributeName, String>> createQueue(String queueName) {
        String msg = null;
        GetQueueAttributesResponse queueAttributesResponse = null;
        CreateQueueResponse createQueueResponse = null;
        try {
            CreateQueueRequest createQueueRequest = CreateQueueRequest.builder().queueName(queueName).build();
            createQueueResponse = sqsClient.createQueue(createQueueRequest);
        } catch (QueueNameExistsException e) {
            throw new SqsServiceException(Constants.BAD_REQUEST_STATUS_CODE, e.getMessage());
        } finally {
            GetQueueUrlRequest queueUrlRequest = GetQueueUrlRequest.builder()
                    .queueName(queueName)
                    .build();
            GetQueueUrlResponse queueUrlResponse = sqsClient.getQueueUrl(queueUrlRequest);
            String queueUrl = queueUrlResponse.queueUrl();
            GetQueueAttributesRequest queueAttributesRequest = GetQueueAttributesRequest.builder()
                    .queueUrl(queueUrl)
                    .attributeNamesWithStrings(QueueAttributeName.ALL.toString())
                    .build();
            queueAttributesResponse = sqsClient.getQueueAttributes(queueAttributesRequest);
        }
        String queueUrl = createQueueResponse.queueUrl();
        logger.info("Queue created: {}", createQueueResponse.queueUrl());
        msg = String.format("Queue created successfully: %s", queueUrl.substring(queueUrl.lastIndexOf("/") + 1));
        return new Response<>(Utility.getCurrentDate(), Constants.CREATED_STATUS_CODE, Constants.SUCCESS_TAG, msg, queueAttributesResponse.attributes());
    }

    @Override
    public Response<List<String>> listQueue() {
        String msg = null;
        ListQueuesRequest listQueuesRequest = ListQueuesRequest.builder().build();
        ListQueuesResponse listQueuesResponse = sqsClient.listQueues(listQueuesRequest);
        List<String> queueNameList = listQueuesResponse.queueUrls().stream().map(url -> url.substring(url.lastIndexOf("/") + 1)).toList();
        msg = "Fetching all queue list successfully";
        return new Response<>(Utility.getCurrentDate(), Constants.OK_STATUS_CODE, Constants.SUCCESS_TAG, msg, queueNameList);
    }

    @Override
    public Response<Map<QueueAttributeName, String>> queueInfo(String queueName) {
        String msg = null;
        GetQueueAttributesResponse queueAttributesResponse = null;
        try {
            GetQueueUrlRequest queueUrlRequest = GetQueueUrlRequest.builder()
                    .queueName(queueName)
                    .build();
            GetQueueUrlResponse queueUrlResponse = sqsClient.getQueueUrl(queueUrlRequest);
            String queueUrl = queueUrlResponse.queueUrl();
            GetQueueAttributesRequest queueAttributesRequest = GetQueueAttributesRequest.builder()
                    .queueUrl(queueUrl)
                    .attributeNamesWithStrings(QueueAttributeName.ALL.toString())
                    .build();
            queueAttributesResponse = sqsClient.getQueueAttributes(queueAttributesRequest);
            msg = "Fetching queue information successfully";
        } catch (QueueDoesNotExistException e) {
            throw new SqsServiceException(Constants.BAD_REQUEST_STATUS_CODE, e.getMessage());
        }
        return new Response<>(Utility.getCurrentDate(), Constants.OK_STATUS_CODE, Constants.SUCCESS_TAG, msg, queueAttributesResponse.attributes());
    }

    @Override
    public Response<?> deleteQueue(String queueName) {
        String msg = null;
        try {
            GetQueueUrlRequest queueUrlRequest = GetQueueUrlRequest.builder()
                    .queueName(queueName)
                    .build();
            GetQueueUrlResponse queueUrlResponse = sqsClient.getQueueUrl(queueUrlRequest);
            String queueUrl = queueUrlResponse.queueUrl();
            DeleteQueueRequest deleteQueueRequest = DeleteQueueRequest.builder()
                    .queueUrl(queueUrl)
                    .build();
            sqsClient.deleteQueue(deleteQueueRequest);
            msg = String.format("SQS queue deleted successfully: %s", queueName);
        } catch (QueueDoesNotExistException e) {
            throw new SqsServiceException(Constants.BAD_REQUEST_STATUS_CODE, e.getMessage());
        }
        return new Response<>(Utility.getCurrentDate(), Constants.OK_STATUS_CODE, Constants.SUCCESS_TAG, msg, null);
    }

}
