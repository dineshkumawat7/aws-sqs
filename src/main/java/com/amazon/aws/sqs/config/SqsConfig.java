package com.amazon.aws.sqs.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;

import java.net.URI;

@Configuration
public class SqsConfig {

    @Autowired
    private EnvironmentParamConfig env;

    @Bean
    public SqsClient sqsClient() {
        return SqsClient.builder()
                .endpointOverride(URI.create(env.getAwsEndpoint()))
                .region(Region.of(env.getAwsRegion()))
                .credentialsProvider(StaticCredentialsProvider.create
                        (AwsBasicCredentials.create(env.getAwsAccessKey(), env.getAwsAccessKey())))
                .build();
    }
}
