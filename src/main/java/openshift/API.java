package openshift;

import javax.enterprise.inject.Produces;
import javax.inject.Named;

import org.apache.camel.builder.RouteBuilder;

import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sqs.SqsClient;

public class API extends RouteBuilder {

  @Produces 
  @Named("awsSNSClient")
   public SnsClient creteSnsClient() {
    return SnsClient.create();
  }

  @Produces 
  @Named("awsSQSClient")
   public SqsClient createSqsClient() {
    return SqsClient.create();
  }

  @Override
  public void configure() throws Exception {
    
    from("direct:start")
      .routeId("SNS-Poll")
      .to("aws2-sns://test.fifo?amazonSNSClient=#awsSNSClient");

    from("aws2-sqs://test-fifo?amazonSQSClient=#awsSQSClient&delay=50&maxMessagesPerPoll=5")
      .routeId("SQS-client")
      .to("stream:out");

  }

}