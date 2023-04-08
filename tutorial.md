# hello-lambda

- invoke a remote lmabda function
  - aws lambda invoke --invocation-type Event --function-name first-lambda-SimpleHello-fKrCxYTBeyDF outputfie.txt --profile hiyadav-root
  - for async the --invocation-type is Event; for sync it is RequetResponse;

- create a apis which will be called via api-gateway
  - an endpoint is created to create a order whose event is triggered by api-gateway
  - we can execute the endpoint using the postman url: https://rfcq4fdlja.execute-api.ap-south-1.amazonaws.com/Stage/orders
  - run the lambda locally : $sam local start-api --> it start the apigateway server locally
  - we can trigger the api endpoint using apigateway resources endpoint exposed, postman or curl request  [curl -H 'Content-Type: application/json' \
      -d '{"id": 111, "itemName": "iphone 14", "quantity": 14}' \
      -X POST \
      https://3ka36gtbli.execute-api.ap-south-1.amazonaws.com/Stage/orders]
  - the data from the api-gateway event is send to dynamo db via lambda

- Second use-case:
  - The data when added to the S3 bucket event trigger the lambda function.
  - Data in s3 bucket act as a event
  - The Data from lambda is send to sns topic
  - SNS Topic act as event trigger for second lambda
  - As message is added to topic it trigger the lambda


## Error handling with the Lambda

* We can use the context to log error.
* We can use log4j dependency also but need to see it as has zero day defect in it.
* It is easy in case of synchronous event but for async it causes issue as we do not want it to just fail. We can retry upto 2 times and than if it fails we can add it to the dead letter queue.
* It is best practise to sanitize the error message logged.

* We can scale the lambda function by running the multiple threads.
* Cold start is the lambda reality which can not be ignored initally execution steup of environment time in order to run the function exceution is termed as cold start. Similarly we have warm start in which the environment setup is in stand-by state waiting for the lambda invoction. Releavant Link:
https://aws.amazon.com/blogs/compute/operating-lambda-performance-optimization-part-1/
* Lambda environment is responsible to run only one event and only it enter the frozen state the other event can be send to it.
* For multi-threading lambda function it is important that the lambda function executed all the thread before it return the lambda execution result. if the thread are not completed before the lambda function is completed, the thread will get frozen with it and when the new request come it will resume again.