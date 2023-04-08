package com.aws.lambda.api;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.aws.lambda.model.OrderDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class OrderService {

        private final ObjectMapper objectMapper = new ObjectMapper();
        private final DynamoDB dynamoDB = new DynamoDB(AmazonDynamoDBClientBuilder.defaultClient());

        /**
         * @param request
         * @return
         * @throws JsonMappingException
         * @throws JsonProcessingException
         */
        public APIGatewayProxyResponseEvent createOrder(APIGatewayProxyRequestEvent request)
                        throws JsonMappingException, JsonProcessingException {
                OrderDto order = objectMapper.readValue(request.getBody(), OrderDto.class);

                Table table = dynamoDB.getTable(System.getenv("ORDERS_TABLE"));
                Item item = new Item().withPrimaryKey("id", order.getId())
                                .withString("itemId", order.getItemName())
                                .withInt("quantity", order.getQuantity());
                table.putItem(item);
                return new APIGatewayProxyResponseEvent().withStatusCode(202)
                                .withBody("created order with id: " + order.getId());
        }

        /**
         * @param request
         * @return
         * @throws JsonMappingException
         * @throws JsonProcessingException
         */
        public APIGatewayProxyResponseEvent retrieveOrder(APIGatewayProxyRequestEvent request)
                        throws JsonMappingException, JsonProcessingException {
                ObjectMapper objectMapper = new ObjectMapper();
                OrderDto dto = new OrderDto(111, "Apple pad", 5);
                String order = objectMapper.writeValueAsString(dto);

                return new APIGatewayProxyResponseEvent().withStatusCode(200)
                                .withBody(order);
        }
}