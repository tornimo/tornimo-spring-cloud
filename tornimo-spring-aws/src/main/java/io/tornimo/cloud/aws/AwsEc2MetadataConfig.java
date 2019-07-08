package io.tornimo.cloud.aws;

public class AwsEc2MetadataConfig {

    private final boolean region;
    private final boolean availabilityZone;
    private final boolean instanceId;
    private final boolean account;
    private final boolean instanceType;

    public AwsEc2MetadataConfig(boolean region,
                                boolean availabilityZone,
                                boolean instanceId,
                                boolean account,
                                boolean instanceType) {
        this.region = region;
        this.availabilityZone = availabilityZone;
        this.instanceId = instanceId;
        this.account = account;
        this.instanceType = instanceType;
    }

    public boolean isRegion() {
        return region;
    }

    public boolean isAvailabilityZone() {
        return availabilityZone;
    }

    public boolean isInstanceId() {
        return instanceId;
    }

    public boolean isAccount() {
        return account;
    }

    public boolean isInstanceType() {
        return instanceType;
    }

    public static AwsEc2MetadataConfig defaults() {
        return new AwsEc2MetadataConfig(true, true, true, false, true);
    }

    public static AwsEc2MetadataConfig full() {
        return new AwsEc2MetadataConfig(true, true, true, true, true);
    }

    public static AwsEc2MetadataConfig none() {
        return new AwsEc2MetadataConfig(false, false, false, false, false);
    }

}
