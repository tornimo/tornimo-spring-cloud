[![Build Status](https://circleci.com/gh/tornimo/tornimo-spring-cloud.svg?style=shield)](https://circleci.com/gh/tornimo/tornimo-spring-cloud)


# tornimo-spring-cloud
Set of utilities for configuring [micrometer.io](micrometer.io) for [tornimo.io](tornimo.io) connector in the cloud.
Check out our repository containing the original [tornimo-spring-reporter](https://github.com/tornimo/tornimo-spring-reporter) plugin.

## Start Using Tornimo.io
1) If you haven't done so, signup for a free account at [tornimo.io](https://tornimo.io/start-free-trial/)
2) After a few minutes, you will receive a link to your dashboard, URL for data ingestion and a token. Use the token and the URL in the configuration step.
3) Add [tornimo-spring-reporter](https://github.com/tornimo/tornimo-spring-reporter) to your dependencies.
4) Add one of tornimo-spring-cloud auto configurations to the project

## Base Configuration
``` 
management.metrics.export.tornimo.token=b55670fd-ea50-40e4-9cf3-82d2ed46c629  # 1
management.metrics.export.tornimo.host=put.b55670fd.tornimo.io                # 2
management.metrics.export.tornimo.app=example-app                             # 3
``` 
1) Token provided during registration at [tornimo.io](tornimo.io)
2) URL for metrics ingestion, provided during registration
3) Application name that will be added to the metric path

## Dependency
Add dependency for [tornimo-spring-reporter](https://github.com/tornimo/tornimo-spring-reporter)
Maven:
```
<dependency>
    <groupId>io.tornimo</groupId>
    <artifactId>tornimo-spring-reporter</artifactId>
    <version>0.2.1</version>
</dependency>
```
Gradle:
```
compile group: 'io.tornimo', name: 'tornimo-spring-reporter', version: '0.1.0'
```

### tornimo-spring-aws
tornimo-spring-aws is a set of utilities that will configure your [micrometer.io](micrometer.io) to work in the AWS environment.

#### AWS Fargate and ECS
AWS [Fargate](https://aws.amazon.com/fargate/) and [ECS](https://aws.amazon.com/ecs/) are services allowing you to run your Docker containers in the cloud. tornimo-spring-aws implements utilities that give you access to the environment information like
region, cluster name, task id, etc.
To configure the plugin all you have to do is add this dependency to your maven project:  

```
<dependency>
    <groupId>io.tornimo</groupId>
    <artifactId>tornimo-spring-aws</artifactId>
    <version>0.2.1</version>
</dependency
```
or for gradle project:
```
compile group: 'io.tornimo', name: 'tornimo-spring-reporter', version: '0.1.0'
```

And one of these lines to the configuration
```
tornimo.aws-ecs-v2.enabled = true  # 1
tornimo.aws-ecs-v3.enabled = true  # 2
```
1) enables AWS ECS or Fargate for version 2 [metadata endpoint](https://docs.aws.amazon.com/AmazonECS/latest/developerguide/task-metadata-endpoint.html),
2) enables AWS ECS or Fargate for version 3 [metadata endpoint](https://docs.aws.amazon.com/AmazonECS/latest/developerguide/task-metadata-endpoint.html).

AWS ECS v2 and v3 support Cluster, TaskARN and Revision properties and add them to your metric path. 
For example your GC metric will look like this:
```
$app.aws-ecs.$region.$cluster_name.$task_id.jvmGcPause.action.*.cause.*.m1_rate
```

#### Demo Dashboard
We provide you with a [demo dashboard](https://demo.tornimo.io/d/iYJJmNnZz/spring-demo-application?orgId=1&from=now-6h&to=now) that displays some key metrics.
The same dashboard is also provided as a ready to use template.

