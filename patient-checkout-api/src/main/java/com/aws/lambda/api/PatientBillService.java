package com.aws.lambda.api;

import com.amazonaws.services.lambda.runtime.events.SNSEvent;
import com.aws.lambda.model.PatientDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class PatientBillService {
    
    private ObjectMapper objectMapper = new ObjectMapper();
    public void handler(SNSEvent event){
        event.getRecords().forEach(snsR -> {
            try {
                PatientDto readValue = objectMapper.readValue(snsR.getSNS().getMessage(), PatientDto.class);
                System.out.println(readValue);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        });
        
    }
}
