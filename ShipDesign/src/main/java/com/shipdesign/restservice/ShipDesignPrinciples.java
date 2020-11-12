package com.shipdesign.restservice;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;


public class ShipDesignPrinciples {

	static Logger logger;
	static {
		logger = LogManager.getLogger(ShipDesignPrinciples.class);
	}

	public static float getWeightOfShipPowerPlant(float displacement) {
		float weightP04 = Constants.P04_PART_OF_DISPLAICEMENT * displacement; //  TBD
		logger.debug("weightP04: {}", weightP04);
		return weightP04;
	}

	public static float getWeightOfShipPowerPlantChangedPrototype(float p04Prototype, float speedOfPrototype, float speedOfProject ) {
		float weight = (float) (p04Prototype * Math.pow(speedOfProject, 3.0f) / Math.pow(speedOfPrototype, 3.0f));
		logger.debug("weight: {}", weight);
		return weight;
	}

	public static float getLoadOfChangedPrototype(float loadOfPrototype, float p4WeightOfChangedPrototype, float p4WeightOfPrototype ) {
		float load  = loadOfPrototype - (p4WeightOfChangedPrototype - p4WeightOfPrototype);
		logger.debug("loadOfPrototype: {}", loadOfPrototype);
		return load;
	}

	public static float getCoefficientOfShipCarryingLoad(float load, float displacement ) {
		float coefficientOfShipCarryingLoad = load/displacement;
		logger.debug("coefficientOfShipCarryingLoad: {}", coefficientOfShipCarryingLoad);
		return coefficientOfShipCarryingLoad;
	}

	public static float getDisplacementByPrototypeCarryingLoadCoefficient(float shipLoad,
																	   float prototypeCarryingLoadCoefficient) {
		float displacement = shipLoad/prototypeCarryingLoadCoefficient;
		logger.debug("displacement: {}", displacement);
		return displacement;
	}

	//Ship Relative Length
	public static float getShipRelativeLength(float speed) {
		float relativeLength = (float)(1.36 * Math.sqrt(speed));
		logger.debug("relativeLength: {}", relativeLength);
		return relativeLength;
	}

	public static float getShipLenghByRelativeLength(float relativeLength, float displacement ) {
		float length = (float)(relativeLength * Math.pow((displacement / Constants.WATER_DENSITY), 0.33f));
		logger.debug("length: {}", length);
		return length;
	}

	//Froude Number
	public static float getFroudeNumber(float speed, float length) {
		float froudeNumber =  (float) (speed / Math.sqrt(Constants.GRAVITY_ACCELERATION * length));
		logger.debug("FroudeNumber: {}", froudeNumber);
		return froudeNumber;
	}

	// coefficient of the overall completeness of the vessel
	public static float getShipDelta(float Froude) {
		float delta = (float) (0.395 / Math.pow(Froude, 0.33f));
		logger.debug("delta: {}", delta);
		return delta;
	}

	//getShipWidth
	public static float getShipWidth(float projectDisplacement,
							  float projectDelta,
							  float projectLength,
							  float prototypeWidth,
							  float prototypeDraft) {
		float projectWidth = (float) Math.sqrt((projectDisplacement / Constants.WATER_DENSITY)
				* (prototypeWidth / prototypeDraft) / (projectDelta * projectLength));
		logger.debug("projectWidth: {}", projectWidth);
		return projectWidth;
	}

	public static float getShipDraft( float projectDisplacement,
							   float projectDelta,
							   float projectLength,
							   float prototypeWidth) {
		float projectWidth = projectDisplacement /(Constants.WATER_DENSITY * Constants.COEFF_PROTRUDING_PARTS
					* projectDelta * projectLength  * prototypeWidth);
		logger.debug("projectWidth: {}", projectWidth);
		return projectWidth;
	}

	public static float getShipBoardHeight (float projectDraft,
								  float prototypeDraft,
								  float prototypeBoardHeight) {
		float projectBoardHeight = prototypeDraft / projectDraft * prototypeBoardHeight;
		logger.debug("projectBoardHeight: {}", projectBoardHeight);
		return projectBoardHeight;
	}

	public static float getAlfaByDelta (float delta) {
		float alfa = (float)(0.98 * Math.pow(delta, 0.5f));
		logger.debug("alfa: {}", alfa);
		return alfa;
	}

	public static float getDisplacementByCapacity(float alfa, float delta, float width, float boardheight, float capacity) {
		float displacement = Constants.WATER_DENSITY * capacity / (1 + ((alfa/delta) * ((boardheight/width) - 1)));
		logger.debug("displacement: {}", displacement);
		return displacement;
	}
	
	public static float getCapacityByPrototype(float protoLoad, float protoCapacity, float projectLoad) {
		float capacity = protoCapacity * projectLoad / protoLoad;
		return capacity;
	}
}
