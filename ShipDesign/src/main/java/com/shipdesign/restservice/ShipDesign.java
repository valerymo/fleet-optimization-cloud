package com.shipdesign.restservice;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ShipDesign {

//	private static ShipDesign INSTANCE;
//
//	private ShipDesign() {
//	}
//
//	public synchronized static ShipDesign getInstance() {
//		if (INSTANCE == null) {
//			INSTANCE = new ShipDesign();
//		}
//		return INSTANCE;
//	}


	public ShipMainTechnicalEconomicCharacteristics getShipMainDetails(
			ShipDesignRequestObject shipDesignRequestObject)
	{

		ShipMainTechnicalEconomicCharacteristics prototypeData = mapRequestDataToProto(shipDesignRequestObject);
		
		ShipMainTechnicalEconomicCharacteristics projData = mapRequestDataToProj(shipDesignRequestObject);
	
		if (prototypeData.getName().equals(projData.getName())){
			projData = prototypeData;
		}
		else {
			if (prototypeData.getSpeed() == projData.getSpeed()) {
				projData = computeShipMainDetailsSpeedConst(projData, prototypeData);
			} else {
				projData = computeShipMainDetailsSpeedVar(projData,prototypeData);
			}
		}
		
		return projData;
	}

	private ShipMainTechnicalEconomicCharacteristics mapRequestDataToProto(ShipDesignRequestObject designRequest) {
		ShipMainTechnicalEconomicCharacteristics shipData = new ShipMainTechnicalEconomicCharacteristics();
		shipData.setName(designRequest.getProto_name());
		shipData.setProject_name(designRequest.getProto_projname());
		shipData.setClassification_society(designRequest.getProto_classification_society());
		shipData.setIce_class(designRequest.getProto_ice_class());
		shipData.setSpeed(Float.parseFloat(designRequest.getProto_speed()));
		shipData.setLoad(Float.parseFloat(designRequest.getProto_load()));
		shipData.setLength(Float.parseFloat(designRequest.getProto_length()));
		shipData.setWidth(Float.parseFloat(designRequest.getProto_width()));
		shipData.setDraft(Float.parseFloat(designRequest.getProto_draft()));
		shipData.setD(Float.parseFloat(designRequest.getProto_D()));
		shipData.setCargo_types(designRequest.getProto_cargo_types());
		
//		ObjectMapper objectMapper = new ObjectMapper();
//		shipData = objectMapper.readValue(designRequest, ShipMainTechnicalEconomicCharacteristics.class);
		
		return shipData;
	}
	
	private ShipMainTechnicalEconomicCharacteristics mapRequestDataToProj(ShipDesignRequestObject designRequest) {
		ShipMainTechnicalEconomicCharacteristics shipData = new ShipMainTechnicalEconomicCharacteristics();
		shipData.setName(designRequest.getProj_name());
		shipData.setSpeed(Float.parseFloat(designRequest.getProj_speed()));
		shipData.setLoad(Float.parseFloat(designRequest.getProj_load()));
		return shipData;
	}

	private ShipMainTechnicalEconomicCharacteristics computeShipMainDetailsSpeedConst(
									ShipMainTechnicalEconomicCharacteristics projData, 
									ShipMainTechnicalEconomicCharacteristics prototypeData)
	{
		ShipMainTechnicalEconomicCharacteristics proj = new ShipMainTechnicalEconomicCharacteristics();
		proj = projData;
		ShipMainTechnicalEconomicCharacteristics proto = new ShipMainTechnicalEconomicCharacteristics();

		float fullDisplacement = proto.getD() * proj.getLoad() / proto.getLoad();
		float length = (float) (proto.getLength() * Math.pow((proto.getD() / fullDisplacement), 0.33f));
		float width = length * proto.getWidth() / proto.getLength();
		float draft = width * proto.getDraft() / proto.getWidth();
		//float proj_H = proj_draft * prototype.H / prototype.T;
		float proj_delta = fullDisplacement / (length * width * draft);
		proj.setLength(length);
		proj.setWidth(width);
		proj.setDraft(draft);
		proj.setD(fullDisplacement);
		proj.setDelta(proj_delta);
		
		return proj;
		//return updateShipMainTechnicalEconomicCharacteristicsByCargoCapacity(prototype, proj);
	}
	

	private ShipMainTechnicalEconomicCharacteristics computeShipMainDetailsSpeedVar(
									ShipMainTechnicalEconomicCharacteristics projData, 
									ShipMainTechnicalEconomicCharacteristics prototypeData)
	{
	
		ShipMainTechnicalEconomicCharacteristics proj = new ShipMainTechnicalEconomicCharacteristics();
		proj = projData;
		ShipMainTechnicalEconomicCharacteristics proto = new ShipMainTechnicalEconomicCharacteristics();

		
		float p04Proto = ShipDesignPrinciples.getWeightOfShipPowerPlant(proto.getD());
		float p04ProtoChanged = ShipDesignPrinciples.getWeightOfShipPowerPlantChangedPrototype(p04Proto, proto.getSpeed(), proj.getSpeed());
		float p15ProtoChanged = ShipDesignPrinciples.getLoadOfChangedPrototype(proto.getLoad(), p04ProtoChanged, p04Proto); 
		float prototypeCarryingCapacityCoefficient = p15ProtoChanged/proto.getD(); 
	
		float l_proj = ShipDesignPrinciples.getShipRelativeLength(proj.getSpeed());
		proj.setD(ShipDesignPrinciples.getDisplacementByPrototypeCarryingCapacityCoefficient(proj.getLoad(), prototypeCarryingCapacityCoefficient));
		proj.setLength(ShipDesignPrinciples.getShipLenghByRelativeLength(l_proj, proj.getD()));
		float froudeNumber = ShipDesignPrinciples.getFroudeNumber(proj.getSpeed(), proj.getLength());
		proj.setDelta(ShipDesignPrinciples.getShipDelta(froudeNumber));
		proj.setWidth(ShipDesignPrinciples.getShipWidth(proj.getD(),proj.getDelta(), proj.getLength(), proto.getWidth(), proto.getDraft()));
		proj.setDraft(ShipDesignPrinciples.getShipDraft(proj.getD(), proj.getDelta(), proj.getLength(), proj.getWidth()));
		//proj.H = ShipDesignPrinciples.getShipBoardHeight(proj.T, prototype.T, prototype.H);
	
		//return updateShipMainTechnicalEconomicCharacteristicsByCargoCapacity(prototype, proj);
		return proj;
		
	}


//	private ShipMainTechnicalEconomicCharacteristics updateShipMainTechnicalEconomicCharacteristicsByCargoCapacity(
//			ShipMainTechnicalEconomicCharacteristics prototype,
//			ShipMainTechnicalEconomicCharacteristics characteristics)
//	{
//
//		if (prototype.Wk > 0) {
//			characteristics.Wk = prototype.Wk * characteristics.Pgr / prototype.Pgr;
//		}
//		else {
//			characteristics.Wk = (int) (Constants.RELATIVE_CARGO_CAPACITY * characteristics.Pgr); //TBD  stub: ÑƒÐ´ÐµÐ»ÑŒÐ½Ð°Ñ� Ð³Ñ€ÑƒÐ·Ð¾Ð²Ð¼ÐµÑ�Ñ‚Ð¸Ð¼Ð¾Ñ�Ñ‚ÑŒ = 1.5
//		}
//
//
//		float alfa = ShipDesignPrinciples.getAlfaByDelta(characteristics.getDelta());
//		float D = ShipDesignPrinciples.getDisplacement(alfa, characteristics);
//		if (D < characteristics.getD()) {
//			alfa += 0.06;
//			D = ShipDesignPrinciples.getDisplacement(alfa, characteristics);
////			if (D < characteristics.D){ TBD TBD TBD TBD
////				HT = 1 + ((m_delta/alfa)*((Wgr/V)-1));
////				dHT = HT - m_HT;
////			}
//		}
//
//		return characteristics;
//	}



}