package io.tornimo.cloud.aws;

import io.tornimo.tokens.TornimoRequestedTokens;
import io.tornimo.tokens.TornimoTokenDefinition;
import io.tornimo.tokens.TornimoTokenValues;

import java.io.InputStream;
import java.net.URI;
import java.util.LinkedHashMap;
import java.util.Map;

public class AwsEcsTokenFactory {

    public static final String AWS_ECS_CLUSTER = "aws:ecs:cluster";
    public static final String AWS_ECS_VERSION = "aws:ecs:version";
    public static final String AWS_ARN_PARTITION = "aws:arn:partition";
    public static final String AWS_ARN_SERVICE = "aws:arn:service";
    public static final String AWS_ARN_REGION = "aws:arn:region";
    public static final String AWS_ARN_ACCOUNT = "aws:arn:account";
    public static final String AWS_ARN_RESOURCETYPE = "aws:arn:resourcetype";
    public static final String AWS_ARN_RESOURCE = "aws:arn:resource";
    public static final String AWS_ARN_QUALIFIER = "aws:arn:qualifier";

    public static TornimoTokenValues getTokens(TornimoRequestedTokens requestedTokens) {
        String service = "http://localhost:51678/v1/metadata";
        try (InputStream stream = new URI(service).toURL().openStream()) {
            return getTokens(requestedTokens, IOUtils.toString(stream));
        } catch (Exception e) {
            throw new RuntimeException("Failed to invoke " + service, e);
        }
    }

    public static TornimoTokenValues getTokens(TornimoRequestedTokens requestedTokens, String json) {
        return getTokens(getMetadata(requestedTokens, json), requestedTokens);
    }

    static AwsEcsMetadata getMetadata(TornimoRequestedTokens requestedTokens, String json) {
        return AwsEcsMetadata.formJson(json,
                requestedTokens.isTokenRequested("aws:ecs:cluster"),
                requestedTokens.isTokenRequested("aws:ecs:version"),
                requestedTokens.groupRequestedAsRequired("aws:arn"));
    }

    static TornimoTokenValues getTokens(AwsEcsMetadata metadata, TornimoRequestedTokens tornimoRequestedTokens) {
        Map<String, String> tokenValues = new LinkedHashMap<>();
        setToken(tornimoRequestedTokens, tokenValues, metadata.getCluster(), AWS_ECS_CLUSTER);
        setToken(tornimoRequestedTokens, tokenValues, metadata.getVersion(), AWS_ECS_VERSION);

        AwsArn awsArn = metadata.getAwsArn();

        setToken(tornimoRequestedTokens, tokenValues, awsArn.getPartition(), AWS_ARN_PARTITION);
        setToken(tornimoRequestedTokens, tokenValues, awsArn.getService(), AWS_ARN_SERVICE);
        setToken(tornimoRequestedTokens, tokenValues, awsArn.getRegion(), AWS_ARN_REGION);
        setToken(tornimoRequestedTokens, tokenValues, awsArn.getAccount(), AWS_ARN_ACCOUNT);
        setToken(tornimoRequestedTokens, tokenValues, awsArn.getResourceType(), AWS_ARN_RESOURCETYPE);
        setToken(tornimoRequestedTokens, tokenValues, awsArn.getResource(), AWS_ARN_RESOURCE);
        setToken(tornimoRequestedTokens, tokenValues, awsArn.getQualifier(), AWS_ARN_QUALIFIER);

        return new TornimoTokenValues(tokenValues);
    }

    private static void setToken(TornimoRequestedTokens tornimoRequestedTokens, Map<String, String> tokenValues, String value, String tokenName) {
        TornimoTokenDefinition tokenDefinition = tornimoRequestedTokens.getTokenDefinition(tokenName);
        if (tokenDefinition != null) {
            if (!value.isEmpty()) {
                tokenValues.put(tokenName, value);
            } else {
                if (!tokenDefinition.getDefaultValue().isEmpty()) {
                    tokenValues.put(tokenName, tokenDefinition.getDefaultValue());
                }
                if (tokenDefinition.isRequired()) {
                    throw new IllegalArgumentException("Token '" + tokenName + "' required but not found.");
                }
            }
        }
    }
}

