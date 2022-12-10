package com.remember.support

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain
import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import com.amazonaws.services.sqs.AmazonSQSAsync
import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder
import org.springframework.cloud.aws.messaging.config.SimpleMessageListenerContainerFactory
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.core.task.AsyncTaskExecutor

@Configuration
class AwsResourceConfiguration {

    @Bean
    @Profile("prod", "bravo")
    fun amazonS3Client(): AmazonS3Client {
        return AmazonS3ClientBuilder.standard()
            .withCredentials(DefaultAWSCredentialsProviderChain())
            .build() as AmazonS3Client
    }

    @Bean
    @Profile("prod", "bravo")
    fun amazonSQSAsync(): AmazonSQSAsync {
        return AmazonSQSAsyncClientBuilder
            .standard()
            .withRegion("ap-northeast-1")
            .build()
    }

    @Bean
    @Profile("prod", "bravo")
    fun queueMessagingTemplate(amazonSQSAsync: AmazonSQSAsync): QueueMessagingTemplate {
        return QueueMessagingTemplate(amazonSQSAsync)
    }

    @Bean
    @Profile("prod", "bravo")
    fun simpleMessageListenerContainerFactory(amazonSQSAsync: AmazonSQSAsync, executor: AsyncTaskExecutor): SimpleMessageListenerContainerFactory {
        val factory = SimpleMessageListenerContainerFactory()
        factory.setAmazonSqs(amazonSQSAsync)
        factory.setMaxNumberOfMessages(10)
        factory.setWaitTimeOut(1)
        factory.setTaskExecutor(executor)
        return factory
    }
}
