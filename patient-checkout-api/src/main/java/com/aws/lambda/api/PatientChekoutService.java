package com.aws.lambda.api;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.lambda.runtime.events.SNSEvent.SNSRecord;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.amazonaws.util.StringUtils;
import com.aws.lambda.model.ErrorDTO;
import com.aws.lambda.model.PatientDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class PatientChekoutService {

    /**
     *
     */
    private Logger logger = LogManager.getLogger(PatientChekoutService.class);
    private static final String PATIENT_TOPIC = System.getenv("PATIENT_CHECKOUT_TOPIC");
    private final AmazonS3 s3 = AmazonS3ClientBuilder.defaultClient();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final AmazonSNS amazonSNS = AmazonSNSClientBuilder.defaultClient();

    public void handler(S3Event s3Event, Context context) {
        LambdaLogger logger = context.getLogger();
        logger.log("Document added to the s3");

        s3Event.getRecords().forEach(record -> {
            S3ObjectInputStream objectContent = s3
                    .getObject(record.getS3().getBucket().getName(), record.getS3().getObject().getKey())
                    .getObjectContent();
            try {
                List<PatientDto> patients = Arrays.asList(objectMapper.readValue(objectContent, PatientDto[].class));
                logger.log("Patient record added to s3: " + patients.toString());
                objectContent.close();

                patients.forEach(p -> {
                    try {
                        logger.log("publish the s3 record on topic");
                        amazonSNS.publish(PATIENT_TOPIC, objectMapper.writeValueAsString(p));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                        returnError("503", "error in the mapping of the response!!", logger);
                    }
                });
            } catch (IOException e) {
                // logger.error("Exception is: ", e);
                StringWriter stringWriter = new StringWriter();
                e.printStackTrace(new PrintWriter(stringWriter));
                logger.log(stringWriter.toString());
                throw new RuntimeException();
            }
        });
    }

    public <T> void returnError(String errorCode, String errorMessage, LambdaLogger logger) {
        final ErrorDTO error = new ErrorDTO<>();
        if (!StringUtils.isNullOrEmpty(errorCode)) {
            error.setErrorCode(errorCode);
            error.setErrorMessage(errorMessage);
        }
    }
}