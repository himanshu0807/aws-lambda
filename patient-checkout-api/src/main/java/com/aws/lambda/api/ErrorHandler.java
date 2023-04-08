package com.aws.lambda.api;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.events.SNSEvent;

public class ErrorHandler {
    
    public void handler(SNSEvent event, Context context) {
        LambdaLogger logger = context.getLogger();
        event.getRecords().stream().forEach(r -> logger.log("Dead letter queue event" +r.toString()));
    }
}
