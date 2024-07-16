package com.crio.LearningNavigator.Service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class EasterEggService {
    
    private static final String NUMBERS_API_URL = "http://numbersapi.com/";

    public String getFact(long number){
         RestTemplate restTemplate = new RestTemplate();
         String url=NUMBERS_API_URL+number;
        return restTemplate.getForObject(url, String.class);
    }

}
