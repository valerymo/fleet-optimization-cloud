package com.shipdesign.restservice;


import javax.validation.Valid;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@Validated
public class ShipDesignController {
    
	@GetMapping("/shipdesign/start")
    public void startProcessing(@Valid @RequestBody ShipDesignRequestObject shipDesignRequestObject) 
	{
		System.out.println("Greetings from Ship Design Service!");      
        ShipDesign shipDesign = new ShipDesign();       
		try {
			ShipMainTechnicalEconomicCharacteristics projData = shipDesign.getShipMainDetails(shipDesignRequestObject);
			ObjectMapper objectMapper = new ObjectMapper();
			String json = objectMapper.writeValueAsString(projData);
			System.out.println("Ship Characteristics: " + json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }    
}
