package com.acvdesign.restservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ACVDesignPrinciples {
	// references to G.F.Demeshko "ACV Desigh, volumes 1 and 2"
	
	private static Logger logger = LoggerFactory.getLogger(ACVDesignPrinciples.class.getName());

	public static float getRatioFullPowerToFullWeight(float speed) {
		float acvFullPowerToFullWeightRatio = (float) (1.48 * Math.pow(speed, 0.89f));
		logger.debug("Power-To-Weight-Ratio: {}, for ACV speed: {}", acvFullPowerToFullWeightRatio, speed);
		return acvFullPowerToFullWeightRatio;
	}

	public static float getAcvFullWeight(float projPayload, float protoFuel, float protoFullWeight,
			float protoFullPayload) {
		float projFullWeight = protoFullWeight * (projPayload / (protoFullPayload - protoFuel));
		logger.debug("Project full weight: {}", projFullWeight);
		return projFullWeight;
	}

	public static float getMaxSpeed(float speed) {
		float maxSpeed = (float) (1.4 * speed);
		logger.debug("Max Speed: {}", maxSpeed);
		return maxSpeed;
	}

	public static float getFullPower(float projSpeed, float projFullWeight, float protoSpeed, float protoFullWeight,
			float protoFullPower) {
		float protoPowerFactor = protoFullWeight * protoSpeed / protoFullPower;
		logger.debug("protoPowerFactor: {}", protoPowerFactor);
		float projFullPower = (float) (5.042 * projFullWeight * projSpeed / protoPowerFactor);
		logger.debug("Project FullPower: {}", projFullPower);
		return projFullPower;
	}

	public static float getFuelConsumptionFactor(float projFullPower) {
		float fuelConsumptionFactor = 0;
		if (projFullPower < 7000) {
			fuelConsumptionFactor = (float) (0.38 - 0.024 * ((float) projFullPower / 1000 - 2));
		} else {
			fuelConsumptionFactor = (float) (0.25 - 1.43 * ((float) projFullPower / 1000000));
		}
		logger.debug("fuelConsumptionFactor: {}", fuelConsumptionFactor);
		return fuelConsumptionFactor;
	}

	public static float getFuelWeight(float speed, float range, float fuelConsumptionFactor, float fullPower) {
		float projFuelWeight = Constants.MARINE_SAFETY_FACTOR * Constants.REMAINS_OF_LUBRICANTS_AND_FUEL
				* fuelConsumptionFactor * fullPower * ((float) range / speed) / 1000;
		logger.debug("Project full weight: {}", projFuelWeight);
		return projFuelWeight;
	}

	public static float getRelativeLength(float length, float fullWeight) {
		float relativeLength = (float) (length / Math.pow((fullWeight / Constants.WATER_DENSITY), 0.333f));
		logger.debug("relativeLength: {}", relativeLength);
		return relativeLength;
	}

	public static float getLengthByRelativeLength(float protRelativeLength, float fullWeight) {
		float len = (float) (protRelativeLength * Math.pow(fullWeight / Constants.WATER_DENSITY, 0.333f));
		logger.debug("ACV Lengh: {}", len);
		return len;
	}

	public static float getMinACVLengthByH3(float h3) {
		float minLen = (float) (9.7822 * Math.pow((h3), 1.201f));
		logger.debug("ACV Min Lengh: {}", minLen);
		return minLen;
	}

	public static float getAircushionArea(float acLengh, float acWidth, float acLengthToWidthRatio) {
		float acArea = acLengh * acWidth * (1 - Constants.AIR_CUSHION_AREA_FACTOR / acLengthToWidthRatio);
		logger.debug("Aircushion Area: {}", acArea);
		return acArea;
	}

	public static boolean checkAcPressureForGsCriterias(float acArea, ACVMainTechnicalEconomicCharacteristics proj,
			ACVMainTechnicalEconomicCharacteristics proto) {
		//Ref: 10.3, 1.13	
		boolean result = true;
		float lp = (float)(proto.getAircushionLength()/Math.pow((proto.getWeightFull()/Constants.WATER_DENSITY), 0.333f));
		float alfa_p = acArea/(proj.getAircushionLength()*proj.getAircushionWidth());
		float Gs = (float)Math.pow ((Math.sqrt(proj.getAircushionLength()/proj.getAircushionWidth()) /(Math.sqrt(alfa_p) * lp)), 3);
		float Gs1 = (float)(proj.getAircushionPressure()/(Constants.WATER_DENSITY * Constants.GRAVITY_ACCELERATION * Math.sqrt(acArea)));			// (1.13)
		if((Gs<0.01)||(Gs>0.03)||(Gs1<0.01)||(Gs1>0.03)){
			String message = "Gs or Gs1 are out of range. Gs: " + Gs + " Gs1: " + Gs1 + "Valid ranges: 0.01<Gs, Gs1<0.03";				
			logger.debug("{}", message);
			result = false;
		}
		return result;
	}

	public static float getACFlow(ACVMainTechnicalEconomicCharacteristics proj, float acArea) {
	    //(4.18)  Расход воздуха ВП
		float acAirFlow = (float)(0.7*acArea*Math.sqrt(proj.getAircushionSkirtHeight()));
		logger.debug("AC Air flow: {}", acAirFlow);
		return acAirFlow;
	}
	
	public static float getAcAirFlowCriteria(ACVMainTechnicalEconomicCharacteristics proj, float acArea, float acAirFlow) {
		//(1.16) -  безразмерный коэффициент расхода воздуха
		float acAirFlowCriteria =  (float)(acAirFlow/(acArea*Math.sqrt(2*proj.getAircushionPressure()/Constants.AIR_DENSITY)));
		logger.debug("AcAirFlowCriteria: {}", acAirFlowCriteria);
		return acAirFlowCriteria;
	}
	
	public static boolean checkAcAirFlowCriteria(float acAirFlowCriteria) {
		//(1.18)
		boolean result = true;
		if ((acAirFlowCriteria<0.01)||(acAirFlowCriteria>0.025)) { 
			String message = "AcAirFlowCriteria is out of range. Gs: " + acAirFlowCriteria + "Valid range: [0.01, 0.025]";
			logger.debug("{}", message);
			result = false;
		}
		return result;
	}

	public static float getOwnHeavePeriod(ACVMainTechnicalEconomicCharacteristics proj) {
		return (float)(2*3.14*Math.sqrt(proj.getAircushionSkirtHeight()/Constants.GRAVITY_ACCELERATION));
	}

	public static boolean checkLiftPowerRatio(float liftPower, float fullPower) {
		// according to statistics (&4.3) :   34 < liftPowerRatio < 41 %
		boolean result = true;
		float liftPowerRatio = liftPower/fullPower;	
		if ((liftPowerRatio < 0.35) || (liftPowerRatio > 0.45)){ 
			String message = "Lift Power Ratio is out of range: " + liftPowerRatio + "Valid range: [35, 0.45]";
			logger.debug("{}", message);
			result = false;
		}
		return result;
	}


}

