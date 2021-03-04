package com.acvdesign.restservice;

public class ACVDesignRequestObjectMapper {

	public static ACVMainTechnicalEconomicCharacteristics mapRequestDataToProto(ACVDesignRequestObject designRequest) {
		
		ACVMainTechnicalEconomicCharacteristics protoData = new ACVMainTechnicalEconomicCharacteristics();
		protoData.setName(designRequest.getProto_name());
		protoData.setProject_name(designRequest.getProto_project_name());
		protoData.setClassification_society(designRequest.getProto_classification_society());
		protoData.setClass_notations(designRequest.getProto_class_notations());
		protoData.setSpeed(Float.parseFloat(designRequest.getProto_speed()));
		protoData.setLoad(Float.parseFloat(designRequest.getProto_load()));
		protoData.setLength(Float.parseFloat(designRequest.getProto_length()));
		protoData.setWidth(Float.parseFloat(designRequest.getProto_width()));		
		protoData.setRange(Float.parseFloat(designRequest.getProto_range()));
		protoData.setH3(Float.parseFloat(designRequest.getProto_h3()));
		protoData.setWeightFull(Float.parseFloat(designRequest.getProto_weightFull()));
		protoData.setWeightEmpty(Float.parseFloat(designRequest.getProto_weightEmpty()));
		protoData.setAircushionLength(Float.parseFloat(designRequest.getProto_aircushionLength()));
		protoData.setAircushionWidth(Float.parseFloat(designRequest.getProto_aircushionWidth()));
		protoData.setAircushionSkirtHeight(Float.parseFloat(designRequest.getProto_aircushionSkirtHeight()));
		protoData.setAircushionSkirtSegmentHeight(Float.parseFloat(designRequest.getProto_aircushionSkirtSegmentHeight()));
		protoData.setAircushionPressure(Float.parseFloat(designRequest.getProto_aircushionPressure()));
		protoData.setAircushionPressureFactor(Float.parseFloat(designRequest.getProto_aircushionPressureFactor()));
		protoData.setMainEnginesType(designRequest.getProto_mainEnginesType());
		protoData.setMainEnginesNumber(Integer.parseInt(designRequest.getProto_mainEnginesNumber()));
		protoData.setMainEnginesFullContinuousPower(Float.parseFloat(designRequest.getProto_mainEnginesFullContinuousPower()));
		protoData.setPropulsorsNumber(Integer.parseInt(designRequest.getProto_propulsorsNumber()));
		protoData.setPropulsorsType(designRequest.getProto_propulsorsType());
		protoData.setPropulsorsDiameter(Float.parseFloat(designRequest.getProto_propulsorsDiameter()));
		protoData.setPropulsorsRotationFrequency(Integer.parseInt(designRequest.getProto_propulsorsRotationFrequency()));
		protoData.setFansNumber(Integer.parseInt(designRequest.getProto_fansNumber()));
		protoData.setFansType(designRequest.getProto_fansType());
		protoData.setFansDisameter(Float.parseFloat(designRequest.getProto_fansDiameter()));
		protoData.setFansRotationFrequency(Integer.parseInt(designRequest.getProto_fansRotationFrequency()));
		protoData.setFansProductivity(Float.parseFloat(designRequest.getProto_fansProductivity()));
		protoData.setSpeedMaxOnCalmWater(Float.parseFloat(designRequest.getProto_speedMaxOnCalmWater()));
		protoData.setFroudeNumber(Float.parseFloat(designRequest.getProto_froudeNumber()));
		protoData.setPayloadMassFull(Float.parseFloat(designRequest.getProto_payloadMassFull()));
		protoData.setPayloadCarsNum(Integer.parseInt(designRequest.getProto_payloadCarsNum()));
		protoData.setPayloadPassangerNum(Integer.parseInt(designRequest.getProto_payloadPassangerNum()));
		protoData.setFuelReserves(Float.parseFloat(designRequest.getProto_fuelReserves()));
		
		return protoData;
	}


	public static ACVMainTechnicalEconomicCharacteristics mapRequestDataToProj(ACVDesignRequestObject designRequest) {
		ACVMainTechnicalEconomicCharacteristics ACVData = new ACVMainTechnicalEconomicCharacteristics();
		ACVData.setName(designRequest.getProj_name());
		ACVData.setSpeed(Float.parseFloat(designRequest.getProj_speed()));
		ACVData.setLoad(Float.parseFloat(designRequest.getProj_load()));
		ACVData.setPayloadMassFull(Float.parseFloat(designRequest.getProj_load()));
		ACVData.setRange(Float.parseFloat(designRequest.getProj_range()));
		ACVData.setH3(Float.parseFloat(designRequest.getProj_h3()));
		return ACVData;
	}
	
}
