package com.acvdesign.restservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ACVDesignPropulsionSystem {
	private static Logger  logger = LoggerFactory.getLogger(ACVDesignPropulsionSystem.class.getName());
	
	public static ACVMainTechnicalEconomicCharacteristics computeACVPropulsionSystem(
			ACVMainTechnicalEconomicCharacteristics projData, ACVMainTechnicalEconomicCharacteristics proto) {
		
		ACVMainTechnicalEconomicCharacteristics proj = new ACVMainTechnicalEconomicCharacteristics();

		//Определение элементов пропульсивного комплекса
		//Используется методология проектирования СВП - учебник Демешко Г.Ф., главы 5, 10 (10.2.1).
		//"Проектирование судов. АСВП"

		// Принятая схема : два винта в насадке. 
		// Насадка может позволить решить проблему уклона поверхности, горба сопротивления, за счет увеличения тяговых характеристик
		// а также снижает уровень шума и делает винт более безопасным в эксплуатации.

		// Диаметр воздушного винта вычисляется по формуле (10.13):
		// dвв = (31.62*e* R^3/2) / (ro-vozd^1/2*Nvv(1-Vv/202)^3/2)
		// с учетом замечаний и рекомендаций парагр.5.4
		// используются (5.19)...(5.21)
		// е = 1.06 ...1.23   (5.12)
			


		// мощность потребляемая движительным комплексом
		// ===================================================
		// статистические данные, параграф 4.3: на движительный комплекс расходуется ~ 59% полной мощности:
		// -- для SRN4, JEFF
		// в программе нижний предел принят 0.45 (ниже приведенных примеров) в связи 
		// с относительно малыми скоростями СВП, и большей долей подъемного комплекса
		
		float fullPower = proj.getMainEnginesFullContinuousPower();
		float liftPower = proj.getLiftPower();
		float propulsionPower = fullPower - liftPower;
		

		float propulsionFactor = (float)propulsionPower/fullPower;
		String projectValidationNotes = "Problem: PropulsionFactor (propulsionPower/fullPower) is out of range [0.45, 0.6]!\n" + 
				"Propulsion Factor: " + propulsionFactor;
		if ((propulsionFactor<0.45) || (propulsionFactor>0.6)){ 
			logger.error(projectValidationNotes);
			proj.setProjectValidationNotes(projectValidationNotes);
			proj.setProjectValidationStatus(false);
			return proj;
		}


		// Мощность потребляемая воздушным винтом (Nvv)
		float airPropellerPower =  (float)0.97 * propulsionPower; //0.97 - к.п.д. трансмиссии.
		float propulsorsNumber = proj.getPropulsorsNumber();
		if (propulsorsNumber<=0) {
			logger.error("Error. Air Propulsors number: " + propulsorsNumber);
			proj.setProjectValidationNotes("Error. Air Propulsors number: " + propulsorsNumber);
			proj.setProjectValidationStatus(false);
			return proj;
		}
		
		airPropellerPower = airPropellerPower/proj.getPropulsorsNumber(); // 2 - количество движителей

		// Сопротивление движению
		// =========================
		// Kgd - гидродинамическое качество = 16...22 (парагр. 9.3)
		int hydrodynamicQualityFactor = 21; // принимаем
		//float R = ((float)9.91*m_D)/Kgd;
		float motionResistence  = proj.getWeightFull()/hydrodynamicQualityFactor;
		motionResistence = motionResistence/proj.getPropulsorsNumber(); // 2 - количество движителей
		
		//======
		float eFactor = (float)1.06; // 1.06 ...1.23 (5.12)

		float propellerThrust, propellerThrustOnSlope; // Тяга винта на стопе
		float propellerDiameter, propellerDiameterCinsiderSlop; // диаметр винта
		
		double deg = 4;
		propellerThrust = motionResistence/(1- proj.getSpeed()/202); //Тяга винта на стопе  (5.11)
		propellerThrustOnSlope = (float) (proj.getWeightFull()*Math.sin(Math.toRadians(deg))); //Тяга винта на стопе для случая уклона поверхности (5.14)
		propellerThrustOnSlope = propellerThrustOnSlope/proj.getPropulsorsNumber();

		//диаметр винта без учета уклона и без насадки  (10.13)
		propellerDiameter = (float) ((float)(31.62*eFactor*Math.pow(propellerThrust,1.5f))
				/(airPropellerPower * Math.sqrt(Constants.AIR_DENSITY)));


		//Диаметра винта с учетом запаса мощности на преодоление уклона, без насадки  (10.13), (5.11)
		//========================================================
		propellerDiameterCinsiderSlop = (float) ((float)(31.62*eFactor*Math.pow(propellerThrustOnSlope,1.5f))
				/(airPropellerPower * Math.sqrt(Constants.AIR_DENSITY)));

		if (propellerDiameter > propellerDiameterCinsiderSlop)	// д.б. больше; если нет - ошибка
			propellerDiameter = propellerDiameterCinsiderSlop;
		else {
			projectValidationNotes = "Error calculation propellerDiameter. propellerDiameter: " + propellerDiameter + "propellerDiameter consider Slop: " + propellerDiameterCinsiderSlop;
			logger.error(projectValidationNotes);
			proj.setProjectValidationNotes(projectValidationNotes);
			proj.setProjectValidationStatus(false);
			return proj;
		}
	

		//Расчет диаметра винта в насадке  (5.19)
		//========================================
		// Tstn/Tst = 1.26 (dvvny/dvv)^2/3;    (5.19)
		// Из условия Tstn = Tst (с учетом уклона): 
		// Условный диаметр (диаметр насадки): dvvny = dvv (1/1.26)^3/2 = 0.707dvv;
		// Согласно парагр. 5.4: dvvny = b*dvvn, где dvvn - диаметр винта в насадке;  b = 1.15...1.25 -> 
		// для b = 1.2 -> dvvn = 0.833*dvvny = 0.833*0.707dvv = 0.59dvv 
		// Примем: dvvn ~= 0.6*dvv; 

		//Проверки:
		//===========
		//Проверка компоновки:
		//=======================
		// Принятая на данном этапе реализации программы, предопределенная схема:
		// два винта в насадках, по бортам (в средней части СВП - площадка/проезд по всей длинне)
		// Ширина площадки принимается не менее 0.6 ширины ВП.,  0.4В отводится винтам в насадках.

		if (((propellerDiameter *0.59) > 6)   
			|| (proj.getPropulsorsNumber()*propellerDiameter > 0.6*proj.getWidth())) { // диаметр винта в насадке (! не диаметр насадки !)
			logger.error("Error calculation propellerDiameter. Diameter is too big. propellerDiameter: " + propellerDiameter); 
			return proj;
		}
	
		
		return proj;
	}
}
