package com.shipdesign.restservice;

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
											String prototype_name, 
											String ship_name,
											String ship_load,
											String ship_speed)
	{

		ShipMainTechnicalEconomicCharacteristics prototypeCharacteristics = getPrototypepTechEconomicCharacteristics(prototype_name);
		
		ShipMainTechnicalEconomicCharacteristics characteristics = new ShipMainTechnicalEconomicCharacteristics();
	
		if (prototype_name.equals(ship_name)){
			characteristics = prototypeCharacteristics;
		}
		else {
			if (prototypeCharacteristics.speed == characteristics.speed) {
				characteristics = computeShipMainDetailsSpeedConst(prototypeCharacteristics, ship_name, ship_load, ship_speed);
			} else {
				characteristics = computeShipMainDetailsSpeedVar(prototypeCharacteristics, ship_name, ship_load, ship_speed);
			}
		}
		
		return characteristics;
	}

	private ShipMainTechnicalEconomicCharacteristics getPrototypepTechEconomicCharacteristics(String prototype_name) {
	// TODO Auto-generated method stub
	//take from DB -- TBD - Couchbase DB
	return null;
}

	private ShipMainTechnicalEconomicCharacteristics computeShipMainDetailsSpeedConst(
						ShipMainTechnicalEconomicCharacteristics prototype,
						String ship_name, String ship_load, String ship_speed)
	{
		ShipMainTechnicalEconomicCharacteristics characteristics = new ShipMainTechnicalEconomicCharacteristics();
		characteristics.name = ship_name;
		characteristics.speed = Integer.parseInt(ship_speed);
		characteristics.Pgr = Integer.parseInt(ship_load);

		characteristics.delta = prototype.delta;
		characteristics.D = characteristics.Pgr * prototype.D /  prototype.Pgr;
		characteristics.L = (float) (prototype.L * Math.pow((prototype.D / characteristics.D), 0.33f));
		characteristics.B = characteristics.L * prototype.B / prototype.L;
		characteristics.T = characteristics.B * prototype.T / prototype.B;
		characteristics.H = characteristics.T * prototype.H / prototype.T;

		return updateShipMainTechnicalEconomicCharacteristicsByCargoCapacity(prototype, characteristics);
	}
	

	private ShipMainTechnicalEconomicCharacteristics computeShipMainDetailsSpeedVar(
					ShipMainTechnicalEconomicCharacteristics prototype,
					String ship_name, String ship_load, String ship_speed)
	{
	
		ShipMainTechnicalEconomicCharacteristics characteristics = new ShipMainTechnicalEconomicCharacteristics();
		characteristics.name = ship_name;
		characteristics.speed = Integer.parseInt(ship_speed);
		characteristics.Pgr = Integer.parseInt(ship_load);
		
		float p04Proto = ShipDesignPrinciples.getWeightOfShipPowerPlant(prototype.D);
		float p04ProtoChanged = ShipDesignPrinciples.getWeightOfShipPowerPlantChangedPrototype(p04Proto, prototype.speed, Integer.parseInt(ship_speed));
		float p15ProtoChanged = ShipDesignPrinciples.getLoadOfChangedPrototype(prototype.Pgr, p04ProtoChanged, p04Proto); 
		float prototypeCarryingCapacityCoefficient = p15ProtoChanged/prototype.D; 
	
		float l_proj = ShipDesignPrinciples.getShipRelativeLength(characteristics.speed);
		characteristics.D = ShipDesignPrinciples.getDisplacementByPrototypeCarryingCapacityCoefficient(characteristics.Pgr, prototypeCarryingCapacityCoefficient);
		characteristics.L = ShipDesignPrinciples.getShipLenghByRelativeLength(l_proj, characteristics.D);
		float froudeNumber = ShipDesignPrinciples.getFroudeNumber(characteristics.speed, characteristics.L);
		characteristics.delta = ShipDesignPrinciples.getShipDelta(froudeNumber);
		characteristics.B = ShipDesignPrinciples.getShipWidth(characteristics.D,characteristics.delta, characteristics.L, prototype.B, prototype.T);
		characteristics.T = ShipDesignPrinciples.getShipDraft(characteristics.D, characteristics.delta, characteristics.L, characteristics.B);
		characteristics.H = ShipDesignPrinciples.getShipBoardHeight(characteristics.T, prototype.T, prototype.H);
	
		return updateShipMainTechnicalEconomicCharacteristicsByCargoCapacity(
										prototype, characteristics);
		
	}


	private ShipMainTechnicalEconomicCharacteristics updateShipMainTechnicalEconomicCharacteristicsByCargoCapacity(
			ShipMainTechnicalEconomicCharacteristics prototype,
			ShipMainTechnicalEconomicCharacteristics characteristics)
	{

		if (prototype.Wk > 0) {
			characteristics.Wk = prototype.Wk * characteristics.Pgr / prototype.Pgr;
		}
		else {
			characteristics.Wk = (int) (Constants.RELATIVE_CARGO_CAPACITY * characteristics.Pgr); //TBD  stub: ÑƒÐ´ÐµÐ»ÑŒÐ½Ð°Ñ� Ð³Ñ€ÑƒÐ·Ð¾Ð²Ð¼ÐµÑ�Ñ‚Ð¸Ð¼Ð¾Ñ�Ñ‚ÑŒ = 1.5
		}


		float alfa = ShipDesignPrinciples.getAlfaByDelta(characteristics.delta);
		float D = ShipDesignPrinciples.getDisplacement(alfa, characteristics);
		if (D < characteristics.D) {
			alfa += 0.06;
			D = ShipDesignPrinciples.getDisplacement(alfa, characteristics);
//			if (D < characteristics.D){ TBD TBD TBD TBD
//				HT = 1 + ((m_delta/alfa)*((Wgr/V)-1));
//				dHT = HT - m_HT;
//			}
		}

		return characteristics;
	}



}