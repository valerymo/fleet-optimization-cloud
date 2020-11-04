package com.shipdesign.restservice;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class ShipDesignController {
    
	@GetMapping("/shipdesign/start")
    public void startProcessing(@RequestParam(required = true) String proj_name,
								@RequestParam(required = true) String proj_load,
								@RequestParam(required = true) String proj_speed,
								@RequestParam(required = true) String prototype_name) 
	{
		System.out.println("Greetings from Ship Design Service!");      
        ShipDesign shipDesign = new ShipDesign();       
		try {
			shipDesign.getShipMainDetails(prototype_name, proj_name, proj_load, proj_speed );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }    
}
