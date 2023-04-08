package com.aws.lambda;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.amazonaws.services.lambda.runtime.Context;

public class SimplyHello {
    
    private Double instanceVariable = Math.random();

    private static Double staticVariable = Math.random();

    public SimplyHello() {
        System.out.println("Inside Constructor");
    }

    static {
        System.out.println("Static Block executed!! added to check cold start");
    }

    public String helloLambda(String name){
        return "Hello from Lambda!! --> " +name;
    }

    public void getStream(InputStream inputStream, OutputStream outputStream, Context context) throws IOException, InterruptedException{
        //Thread.sleep(4000); // higher than the default value that is 3 sec
        System.out.println(System.getenv("restapiurl"));
        System.out.println(context.getAwsRequestId());
        System.out.println(context.getInvokedFunctionArn());
        System.out.println(context.getRemainingTimeInMillis());
        int read;
        while((read=inputStream.read()) != -1){
            outputStream.write(Character.toLowerCase(read));
            System.out.println("================");
        }
    }

    // so we initiate the test 3 times to which we see that class is loaded once when the environment is created and for the other 2 it just runs the method
    // hence any global or static varibale is not called. The aws lambda has cooling off period before stopping the lambda function and killing the environment it created.
    public void coldStartBasic(){
        double random = Math.random();
        System.out.println("Instance: "+instanceVariable+" Static variable: " +staticVariable+" localVariable: "+random);
    }
}
  