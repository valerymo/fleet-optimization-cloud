package com.acvdesign.restservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ACVDesign {
	private static Logger  logger = LoggerFactory.getLogger(ACVDesign.class.getName());

	public ACVMainTechnicalEconomicCharacteristics getACVMainDetails(
			ACVDesignRequestObject ACVDesignRequestObject)
	{

		ACVMainTechnicalEconomicCharacteristics prototypeData = ACVDesignRequestObjectMapper.mapRequestDataToProto(ACVDesignRequestObject);
		
		ACVMainTechnicalEconomicCharacteristics projData = ACVDesignRequestObjectMapper.mapRequestDataToProj(ACVDesignRequestObject);
	
		if (prototypeData.getName().equals(projData.getName())){
			projData = prototypeData;
		}
		else {
			if (prototypeData.getSpeed() == projData.getSpeed()) {
				projData = computeACVMainDetailsSpeedConst(projData, prototypeData);
			} else {
				projData = computeACVMainDetailsSpeedVar(projData, prototypeData);
			}
		}
		
		return projData;
	}

	private ACVMainTechnicalEconomicCharacteristics computeACVMainDetailsSpeedConst(
									ACVMainTechnicalEconomicCharacteristics projData, 
									ACVMainTechnicalEconomicCharacteristics proto)
	{
		ACVMainTechnicalEconomicCharacteristics proj = new ACVMainTechnicalEconomicCharacteristics();
		proj = projData;

		getPrototypePropertiesPart(proto, proj);
		
		
		return proj;
	}

	private ACVMainTechnicalEconomicCharacteristics computeACVMainDetailsSpeedVar(
									ACVMainTechnicalEconomicCharacteristics projData, 
									ACVMainTechnicalEconomicCharacteristics proto)
	{
		
		ACVMainTechnicalEconomicCharacteristics projMainDetails = new ACVMainTechnicalEconomicCharacteristics();
		projMainDetails = projData;
		projMainDetails = computeACVMainCharacteristics(projMainDetails, proto);
		if (! projMainDetails.isProjectValidationStatus())
			return projMainDetails;	
		projMainDetails = ACVDesignLiftSystem.computeACVLiftSystem(projMainDetails, proto);
		projMainDetails = ACVDesignPropulsionSystem.computeACVPropulsionSystem(projMainDetails, proto);		
		return projMainDetails;		
	}


	private ACVMainTechnicalEconomicCharacteristics computeACVMainCharacteristics(
			ACVMainTechnicalEconomicCharacteristics projData, ACVMainTechnicalEconomicCharacteristics proto) {
		
		ACVMainTechnicalEconomicCharacteristics proj = new ACVMainTechnicalEconomicCharacteristics();
		proj = projData;
		
		float projFullWeight = ACVDesignPrinciples.getAcvFullWeight(proj.getLoad(),proto.getFuelReserves(), 
												proto.getWeightFull(), proto.getPayloadMassFull());		
		float projFullPower = ACVDesignPrinciples.getFullPower(proj.getSpeed(),projFullWeight,proto.getSpeed(),
												proto.getWeightFull(),proto.getMainEnginesFullContinuousPower());
		float projFuelConsumptionFactor =  ACVDesignPrinciples.getFuelConsumptionFactor(projFullPower);
		float factorNu = (float)(proto.getPayloadMassFull() + proto.getFuelReserves())/proto.getWeightFull();
		float protoFuel2FullWeight = proto.getFuelReserves()/proto.getWeightFull();
		float projRange = proj.getRange();
		String projectValidationNotes = "Validated";

		boolean found = false;
		float fuelWeight = 0;
		float fullWeight = 0; 
		float fuel2FullWeight = 1;
		while (!found){
			fuelWeight = ACVDesignPrinciples.getFuelWeight(proj.getSpeed(),projRange,projFuelConsumptionFactor,projFullPower);				
			fullWeight = (proj.getPayloadMassFull() + fuelWeight)/factorNu;
			fuel2FullWeight = fuelWeight/fullWeight;
			if(fuel2FullWeight > protoFuel2FullWeight)
				projRange -= 10;
			else
				found = true;
		}
		float projRatioFullPowerToFullWeight = ACVDesignPrinciples.getRatioFullPowerToFullWeight(proj.getSpeed());	
		float fullPower = projRatioFullPowerToFullWeight * fuelWeight; //kvt	
		if (projFullPower < fullPower)
			projFullPower = fullPower;

		float protoRelativeLength = ACVDesignPrinciples.getRelativeLength(proto.getLength(), proto.getWeightFull());
		float length = ACVDesignPrinciples.getLengthByRelativeLength(protoRelativeLength, fullWeight);
		float minLengh = ACVDesignPrinciples.getMinACVLengthByH3(proj.getH3());
		if (length < minLengh){
			String acvData = "Name: " + proj.getName() + ", Lengh: " + length + ", minLengh: " + minLengh;
			logger.debug("ACV is too small for current Region. Lengh less than Min. {}", acvData);
			projectValidationNotes = "ACV is too small for current Region. Lengh: " + length + "m is less than Min: "+ minLengh + "m";
			proj.setProjectValidationNotes(projectValidationNotes);
			proj.setProjectValidationStatus(false);
			return proj;
		}
		
		float acLengthToWidthRatio = proto.getAircushionLength()/proto.getAircushionWidth();
		float acWidth = length/acLengthToWidthRatio; //AC length ~= Length
		float acHeight = acWidth * proto.getAircushionSkirtHeight() / proto.getAircushionWidth();
		if (acHeight < projData.getH3()){
			String acvData = "Name: " + projData.getName() + ", acHeight: " + acHeight + ", H3: " + projData.getH3();
			logger.debug("Aircushion Skirt Height is too low for current Region. {}", acvData);
			projectValidationNotes = "Aircushion Skirt Height: " + acHeight + "m, " + 
					"is too low for current Region, H3: " + projData.getH3() + "m";
			proj.setProjectValidationNotes(projectValidationNotes);
			proj.setProjectValidationStatus(false);
			return proj;
		}
		

		proj.setLength(length);
		proj.setWidth(acWidth);
		proj.setAircushionLength(length);
		proj.setAircushionWidth(acWidth);
		proj.setMainEnginesFullContinuousPower(projFullPower);
		proj.setWeightFull(fullWeight);
		proj.setFuelReserves(fuelWeight);
		return proj;
	}




	private ACVMainTechnicalEconomicCharacteristics getPrototypePropertiesPart(ACVMainTechnicalEconomicCharacteristics proto,
			ACVMainTechnicalEconomicCharacteristics proj) {
		
		ACVMainTechnicalEconomicCharacteristics projUpdate = new ACVMainTechnicalEconomicCharacteristics();
		projUpdate = proj;
		projUpdate.setProject_name(proto.getProject_name());
		projUpdate.setClassification_society(proto.getClassification_society());
		projUpdate.setClass_notations(proto.getClass_notations());
		//projUpdate.setIce_class(proto.getIce_class());
		//projUpdate.setCargo_types(proto.getCargo_types());
		return projUpdate;
	}


}