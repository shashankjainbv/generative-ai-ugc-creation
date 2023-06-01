package com.bv.reviewtip;

import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;

@Slf4j
public class SecretManager {

    public static String getSecretValue(String secretKey, String profile, String region, boolean isLocal) {

        SecretsManagerClient client;
        if (isLocal) {
            client = SecretsManagerClient.builder()
                    .credentialsProvider(ProfileCredentialsProvider.create(profile))
                    .region(Region.of(region))
                    .build();
        } else {
            client = SecretsManagerClient.builder().region(Region.of(region)).build();
        }

        GetSecretValueRequest getSecretValueRequest = GetSecretValueRequest.builder()
                .secretId(secretKey)
                .build();

        GetSecretValueResponse getSecretValueResponse;
        try {
            getSecretValueResponse = client.getSecretValue(getSecretValueRequest);
        } catch (Exception e) {
            log.error("Something went wrong while accessing secrets", e);
            throw e;
        }

        return getSecretValueResponse.secretString();
    }
}
