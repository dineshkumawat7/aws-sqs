package com.amazon.aws.sqs.service;

import com.amazon.aws.sqs.dto.Response;
import software.amazon.awssdk.services.sqs.model.QueueAttributeName;

import java.util.List;
import java.util.Map;

public interface SqsService {
    Response createQueue(String queueName);

    Response<List<String>> listQueue();

    Response<Map<QueueAttributeName, String>> queueInfo(String queueName);

    Response<?> deleteQueue(String queueName);
}
