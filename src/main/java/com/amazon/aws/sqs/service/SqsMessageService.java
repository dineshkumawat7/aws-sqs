package com.amazon.aws.sqs.service;

import com.amazon.aws.sqs.dto.Response;

import java.util.List;

public interface SqsMessageService {
    Response<Object> sendMessage(String queueName ,Object object);

    Response<List<Object>> receiveMessage(String queueName);
}
