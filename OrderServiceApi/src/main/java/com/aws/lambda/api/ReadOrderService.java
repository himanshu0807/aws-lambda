package com.aws.lambda.api;

import java.util.List;
import java.util.stream.Collectors;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.aws.lambda.model.OrderDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ReadOrderService {
  private final ObjectMapper objectMapper = new ObjectMapper();
  private final AmazonDynamoDB defaultClient = AmazonDynamoDBClientBuilder.defaultClient();

  /**
   * @param request
   * @return
   * @throws JsonMappingException
   * @throws JsonProcessingException
   */

  public APIGatewayProxyResponseEvent getOrder(APIGatewayProxyRequestEvent request)
      throws JsonMappingException, JsonProcessingException {
    // OrderDto orders = new OrderDto(111, "Apple pad", 5);
    ScanResult scanResult = defaultClient.scan(new ScanRequest().withTableName(System.getenv("ORDERS_TABLE")));
    List<OrderDto> orders = scanResult.getItems().stream()
        .map(i -> new OrderDto(Integer.parseInt(i.get("id").getN()), i.get("itemId").getS(),
            Integer.parseInt(i.get("quantity").getN())))
        .collect(Collectors.toList());
    String jsonOutput = objectMapper.writeValueAsString(orders);

    return new APIGatewayProxyResponseEvent().withStatusCode(200)
        .withBody(jsonOutput);
  }
}
