package com.acvdesign.restservice;


import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@Validated
public class ACVDesignController {
    
	@GetMapping("/acvdesign/start")
    public ResponseEntity<String>  startProcessing(@Valid @RequestBody ACVDesignRequestObject acvDesignRequestObject) 
	{
		System.out.println("Greetings from ACV Design Service!");      
        ACVDesign acvDesign = new ACVDesign();  
        String json ="";
		try {
			//ACVMainTechnicalEconomicCharacteristics projData = acvDesign.getMainDetails(shipDesignRequestObject);
			ACVMainTechnicalEconomicCharacteristics projData = acvDesign.getACVMainDetails(acvDesignRequestObject);
			ObjectMapper objectMapper = new ObjectMapper();
			json = objectMapper.writeValueAsString(projData);
			System.out.println("ACV Characteristics: " + json);
        } catch (Exception e) {
            e.printStackTrace();
        }
		return new ResponseEntity<>(
			      "ACV designed details: " + json, 
			      HttpStatus.OK);
    }    
}
