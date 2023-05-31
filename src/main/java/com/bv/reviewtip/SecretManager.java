package com.bv.reviewtip;

import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;


public class SecretManager {

    public static String getSecretValue(String secretKey, String profile, String region) {

        SecretsManagerClient client = SecretsManagerClient.builder()
                .credentialsProvider(ProfileCredentialsProvider.create(profile))
                .region(Region.of(region))
                .build();

        GetSecretValueRequest valueRequest = GetSecretValueRequest.builder()
                .secretId(secretKey)
                .build();

        GetSecretValueResponse valueResponse = client.getSecretValue(valueRequest);
        return valueResponse.secretString();
    }
}
