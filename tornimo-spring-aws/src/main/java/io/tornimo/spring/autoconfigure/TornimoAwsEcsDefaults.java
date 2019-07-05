package io.tornimo.spring.autoconfigure;

import io.tornimo.cloud.aws.AwsArnConfig;

public class TornimoAwsEcsDefaults {

    public static AwsArnConfig getDefaultArnConfig() {
        boolean partition = false;
        boolean service = false;
        boolean region = true;
        boolean account = false;
        boolean resourceType = false;
        boolean resource = true;
        boolean qualifier = true;
        String defaultPartition = "";
        String defaultService = "";
        String defaultRegion = "region-none";
        String defaultAccount = "";
        String defaultResourceType = "";
        String defaultResource = "resource-none";
        String defaultQualifier = "qualifier-none";

        return new AwsArnConfig(
                partition,
                service,
                region,
                account,
                resourceType,
                resource,
                qualifier,
                defaultPartition,
                defaultService,
                defaultRegion,
                defaultAccount,
                defaultResourceType,
                defaultResource,
                defaultQualifier
        );
    }
}
