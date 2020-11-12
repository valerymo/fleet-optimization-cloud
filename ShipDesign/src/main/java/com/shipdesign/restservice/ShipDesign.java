package com.shipdesign.restservice;

public class ShipDesign {

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
		ShipMainTechnicalEconomicCharacteristics protoData = new ShipMainTechnicalEconomicCharacteristics();
		protoData.setName(designRequest.getProto_name());
		protoData.setProject_name(designRequest.getProto_projname());
		protoData.setClassification_society(designRequest.getProto_classification_society());
		protoData.setIce_class(designRequest.getProto_ice_class());
		protoData.setClass_notations(designRequest.getProto_class_notations());
		protoData.setSpeed(Float.parseFloat(designRequest.getProto_speed()));
		protoData.setLoad(Float.parseFloat(designRequest.getProto_load()));
		protoData.setLength(Float.parseFloat(designRequest.getProto_length()));
		protoData.setWidth(Float.parseFloat(designRequest.getProto_width()));
		protoData.setDraft(Float.parseFloat(designRequest.getProto_draft()));
		protoData.setBoardheight(Float.parseFloat(designRequest.getProto_boardheight()));
		protoData.setD(Float.parseFloat(designRequest.getProto_D()));
		protoData.setCargo_types(designRequest.getProto_cargo_types());
		protoData.setCapacity(Float.parseFloat(designRequest.getProto_capacity()));
		
		return protoData;
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
									ShipMainTechnicalEconomicCharacteristics proto)
	{
		ShipMainTechnicalEconomicCharacteristics proj = new ShipMainTechnicalEconomicCharacteristics();
		proj = projData;

		getPrototypePropertiesPart(proto, proj);
		
		float fullDisplacement = proto.getD() * proj.getLoad() / proto.getLoad();
		float length = (float) (proto.getLength() * Math.pow((fullDisplacement/proto.getD()), 0.33f));
		float width = length * proto.getWidth() / proto.getLength();
		float draft = width * proto.getDraft() / proto.getWidth();
		float boardheight = draft * proto.getBoardheight() / proto.getDraft();
		float proj_delta = fullDisplacement / (length * width * draft);
		proj.setLength(length);
		proj.setWidth(width);
		proj.setDraft(draft);
		proj.setBoardheight(boardheight);
		proj.setD(fullDisplacement);
		proj.setDelta(proj_delta);
		
		return proj;
	}
	

	private ShipMainTechnicalEconomicCharacteristics computeShipMainDetailsSpeedVar(
									ShipMainTechnicalEconomicCharacteristics projData, 
									ShipMainTechnicalEconomicCharacteristics proto)
	{
	
		ShipMainTechnicalEconomicCharacteristics proj = new ShipMainTechnicalEconomicCharacteristics();
		proj = projData;
		
		proj = getPrototypePropertiesPart(proto, proj);
		
		float p04Proto = ShipDesignPrinciples.getWeightOfShipPowerPlant(proto.getD());
		float p04ProtoChanged = ShipDesignPrinciples.getWeightOfShipPowerPlantChangedPrototype(p04Proto, proto.getSpeed(), proj.getSpeed());
		float p15ProtoChanged = ShipDesignPrinciples.getLoadOfChangedPrototype(proto.getLoad(), p04ProtoChanged, p04Proto); 
		float prototypeCarryingLoadCoefficient = p15ProtoChanged/proto.getD(); 
	
		proj.setD(ShipDesignPrinciples.getDisplacementByPrototypeCarryingLoadCoefficient(proj.getLoad(), prototypeCarryingLoadCoefficient));
		float l_proj = ShipDesignPrinciples.getShipRelativeLength(proj.getSpeed());
		proj.setLength(ShipDesignPrinciples.getShipLenghByRelativeLength(l_proj, proj.getD()));
		float froudeNumber = ShipDesignPrinciples.getFroudeNumber(proj.getSpeed(), proj.getLength());
		proj.setDelta(ShipDesignPrinciples.getShipDelta(froudeNumber));
		proj.setWidth(ShipDesignPrinciples.getShipWidth(proj.getD(),proj.getDelta(), proj.getLength(), proto.getWidth(), proto.getDraft()));
		proj.setDraft(ShipDesignPrinciples.getShipDraft(proj.getD(), proj.getDelta(), proj.getLength(), proj.getWidth()));
		proj.setBoardheight(ShipDesignPrinciples.getShipBoardHeight(proj.getDraft(), proto.getDraft(), proto.getBoardheight()));
		proj.setCapacity(ShipDesignPrinciples.getCapacityByPrototype(proto.getLoad(), proto.getCapacity(), proj.getLoad()));

		return proj;		
	}


	private ShipMainTechnicalEconomicCharacteristics getPrototypePropertiesPart(ShipMainTechnicalEconomicCharacteristics proto,
			ShipMainTechnicalEconomicCharacteristics proj) {
		
		ShipMainTechnicalEconomicCharacteristics projUpdate = new ShipMainTechnicalEconomicCharacteristics();
		projUpdate = proj;
		projUpdate.setProject_name(proto.getProject_name());
		projUpdate.setClassification_society(proto.getClassification_society());
		projUpdate.setClass_notations(proto.getClass_notations());
		projUpdate.setIce_class(proto.getIce_class());
		projUpdate.setCargo_types(proto.getCargo_types());
		return projUpdate;
	}


}