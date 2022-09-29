package openshift;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.camel.builder.RouteBuilder;

import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sqs.SqsClient;

@ApplicationScoped
public class API extends RouteBuilder {

  @Inject
  SqsClient awsSNSClient;

  @Inject
  SnsClient awsSQSClient;

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