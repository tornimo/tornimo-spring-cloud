package io.tornimo.cloud.aws;

import com.amazonaws.util.EC2MetadataUtils;

public class AwsEc2Metadata {

    private final String region;
    private final String availabilityZone;
    private final String instanceType;
    private final String instanceId;
    private final String account;

    public static AwsEc2Metadata create() {
        EC2MetadataUtils.InstanceInfo instanceInfo = EC2MetadataUtils.getInstanceInfo();

        String region = EC2MetadataUtils.getEC2InstanceRegion();
        String availabilityZone = EC2MetadataUtils.getAvailabilityZone();
        String instanceType = EC2MetadataUtils.getInstanceType();
        String instanceId = EC2MetadataUtils.getInstanceId();
        String account = instanceInfo.getAccountId();
        
        return new AwsEc2Metadata(region, availabilityZone, instanceType, instanceId, account);
    }

    AwsEc2Metadata(String region,
                   String availabilityZone,
                   String instanceType,
                   String instanceId,
                   String account) {
        this.region = region;
        this.availabilityZone = availabilityZone;
        this.instanceType = instanceType;
        this.instanceId = instanceId;
        this.account = account;
    }

    public String getRegion() {
        return region;
    }

    public String getAvailabilityZone() {
        return availabilityZone;
    }

    public String getInstanceType() {
        return instanceType;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public String getAccount() {
        return account;
    }
}
