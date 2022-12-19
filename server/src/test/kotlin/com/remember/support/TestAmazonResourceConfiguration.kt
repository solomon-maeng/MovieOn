package com.remember.support

import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.client.builder.AwsClientBuilder
import com.amazonaws.regions.Regions
import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.sqs.AmazonSQSAsync
import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.testcontainers.containers.localstack.LocalStackContainer
import org.testcontainers.utility.DockerImageName

//@TestConfiguration
class TestAmazonResourceConfiguration {

    // TODO 추후 작성

    private val localStack = LocalStackContainer(DockerImageName.parse("localstack/localstack:latest"))
    private val credentials =
        AWSStaticCredentialsProvider(BasicAWSCredentials(localStack.accessKey, localStack.secretKey))
    private val region = Regions.AP_NORTHEAST_1

    @Bean
    fun amazonSQSAsyncLocalStack(): AmazonSQSAsync {
        return AmazonSQSAsyncClientBuilder
            .standard()
            .withRegion(region)
            .withCredentials(credentials)
            .build()
    }

    @Bean
    fun amazonS3ClientLocalStack(): AmazonS3Client {
        return AmazonS3Client
            .builder()
            .withEndpointConfiguration(
                AwsClientBuilder.EndpointConfiguration(
                    localStack.getEndpointOverride(
                        LocalStackContainer.Service.S3
                    ).toString(), region.name
                )
            )
            .withRegion(region)
            .withCredentials(credentials)
            .build() as AmazonS3Client
    }
}
