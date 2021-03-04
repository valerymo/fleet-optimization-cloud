package com.acvdesign.restservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ACVDesignLiftSystem {
	private static Logger  logger = LoggerFactory.getLogger(ACVDesignLiftSystem.class.getName());

	public static ACVMainTechnicalEconomicCharacteristics computeACVLiftSystem(
			ACVMainTechnicalEconomicCharacteristics projData, ACVMainTechnicalEconomicCharacteristics proto) {
		
		ACVMainTechnicalEconomicCharacteristics proj = new ACVMainTechnicalEconomicCharacteristics();
		
		float acLengthToWidthRatio = projData.getAircushionLength()/projData.getAircushionWidth();
		
		float acArea = ACVDesignPrinciples.getAircushionArea(projData.getAircushionLength(), 
															 projData.getAircushionWidth(), 
															 acLengthToWidthRatio);
		//Pressure in aircushion
		float acPressure = projData.getWeightFull()/acArea;
		if (!ACVDesignPrinciples.checkAcPressureForGsCriterias(acArea, proj, proto)) {
				logger.debug("Warning: AC pressure is not match to GS criterias.: {}", acPressure);	
		}
		
		//AC Air Flow
		//Расход воздуха в воздушной подушке
		float acAirFlow = ACVDesignPrinciples.getACFlow(proj, acArea);  
		float acAirFlowCriteria = ACVDesignPrinciples.getAcAirFlowCriteria(proj, acArea, acAirFlow);
		if (!ACVDesignPrinciples.checkAcAirFlowCriteria(acAirFlowCriteria)) {
			logger.debug("Warning: AC AirFlow Criteria is out of range");
		}
		
		//Own heave period
		//Собственный период вертикальной качки
		//float ownHeavePeriod = ACVDesignPrinciples.getOwnHeavePeriod(proj);	//(4.15)

		//Time of AC volume filling by Fan
		//Время заполнения объема ВП нагнетателем
		float acFillTime = proj.getAircushionLength() * proj.getAircushionWidth() * proj.getAircushionSkirtHeight() / acAirFlow;
		
		//Power consumed by the lifting complex
		//мощность потребляемая подъемным комплексом
		float liftPower = 3*proj.getAircushionPressure()*acAirFlow;
		
		//check Lift Power ratio
		if (!ACVDesignPrinciples.checkLiftPowerRatio(liftPower, proj.getMainEnginesFullContinuousPower())) {
			logger.debug("Warning: Lift Power Ratio is out of range");
		}

		//Lift system Fans selection
		//Temporary approach (should be improved):
		//Type of Fan is centrifugal -- Tип нагнетателя - ценробежный
		//Number of Fans selected as following:
		//	If prototype is "JEFFA/B":  num=8 
		//  If prototype is "VT": num=8 
		//  else if acWidth>10m: num=4
		//  else num=2;
		
		int fansNumber = 2;
		if((proto.getName().contains("JEFF")) || (proto.getName().contains("VT")))
			fansNumber = 8;
		else if (proto.getAircushionWidth() > 10)
			fansNumber = 4;
		
		float acPressureFactor =  acAirFlow/acPressure;
		float acPressureFactorMin = (float) (acAirFlow / (Constants.AIR_DENSITY) * Math.sqrt(Constants.FAN_BLADE_EDGE_PERIPHERAL_SPEED_LIMIT));
		if(acPressureFactor < acPressureFactorMin) {
			logger.error("Error: acPressureFactor problem");
			return null;
		}

		float liftFanDiameter =  calculateLiftFanDiameter(acPressureFactor, acAirFlow, fansNumber);
		
		String message = "ACV Lift System computed:\nAC Pressure: " + acPressure + ", AC PressureFactor: " + acPressureFactor + ", Fans Number: " 
						  + fansNumber + ", Fan Diameter: " + liftFanDiameter;
		logger.debug("{}", message);
		
		proj.setAircushionPressure(acPressure);
		proj.setAircushionPressureFactor(acPressureFactor);
		proj.setFansNumber(fansNumber);
		proj.setFansDisameter(liftFanDiameter);
		proj.setAircushionFillTime(acFillTime);
		proj.setLiftPower(liftPower);
		
		return proj;
	}

	private static float calculateLiftFanDiameter(float acPressureFactor, float acAirFlow, int fansNumber) {
		//Определение диаметра рабочего колеса нагнетателя
		// используется формула (4.33): Drk = sqrt(4*QH/pi*Qh*sqrt(ro-vozd*H`/H))
		// QH = k3*Qp
		// k3 - учитывает отбор части воздуха из ресивера в систему для питания двигателей,
		// охлаждения, ...  см. параграф 4.3, стр 210.   k3 = 1.05 ... 1.10; 
		// примем к3= 1.07 ? 1.05
		// QH делится на число нагнетателей:  QH = Qp*1.07/m_nagn_num.
		// Qh = 0.10 ... 0.20; (tab 4.2).
		
		// согласно таблицы 4.1, суммарный коэффициент потерь дпвления
		// м.б. принят H/Pp = 1.8 (соответствует JEFFA); H = 1.8*m_Pp; или H/Pp = 2.0

		//kH - коэффициент давления = 0.25..0.5
		//kH_min = H/(ro_vozd*u^2); u_max = 180m/c (4.2.3), (4.26)
		//kH_min = 1.6*m_Pp/(ro_vozd*32400) = m_Pp/(ro_vozd*20250)


		//диаметра рабочего колеса нагнетателя
		float fanDiameter = (float)(Math.sqrt(((4* Constants.AIR_FROM_LIFT_TO_MAIN_ENGINE_FACTOR *acAirFlow)
								/ (Math.PI*fansNumber*Constants.AIR_FLOW_FACTOR)))
								* Math.sqrt(Constants.AIR_DENSITY * acPressureFactor/(acAirFlow)));
		
		return fanDiameter;
	}
		
}
