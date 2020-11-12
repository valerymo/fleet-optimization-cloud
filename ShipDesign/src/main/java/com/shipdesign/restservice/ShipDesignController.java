package com.shipdesign.restservice;


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
public class ShipDesignController {
    
	@GetMapping("/shipdesign/start")
    public ResponseEntity<String>  startProcessing(@Valid @RequestBody ShipDesignRequestObject shipDesignRequestObject) 
	{
		System.out.println("Greetings from Ship Design Service!");      
        ShipDesign shipDesign = new ShipDesign();  
        String json ="";
		try {
			ShipMainTechnicalEconomicCharacteristics projData = shipDesign.getShipMainDetails(shipDesignRequestObject);
			ObjectMapper objectMapper = new ObjectMapper();
			json = objectMapper.writeValueAsString(projData);
			System.out.println("Ship Characteristics: " + json);
        } catch (Exception e) {
            e.printStackTrace();
        }
		return new ResponseEntity<>(
			      "Ship designed details: " + json, 
			      HttpStatus.OK);
    }    
}
