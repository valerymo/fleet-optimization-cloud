package com.shipdesign.restservice;

public class ShipMainTechnicalEconomicCharacteristics {
	
	//Data from Request Object:
    private String name;
    private String project_name;
    private String classification_society;
    private String  class_notations;
    private String ice_class;
    private float speed; 
    private float load;
    private float length;
    private float width;
    private float draft;
    private float boardheight;
    private float D;
    private String cargo_types;
    private float capacity;
    
    //Calculated data:
    private float delta;
    
    // Calculated data end
    
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getClassification_society() {
		return classification_society;
	}
	public void setClassification_society(String classification_society) {
		this.classification_society = classification_society;
	}
	public String getClass_notations() {
		return class_notations;
	}
	public void setClass_notations(String class_notations) {
		this.class_notations = class_notations;
	}
	public String getProject_name() {
		return project_name;
	}
	public void setProject_name(String project_name) {
		this.project_name = project_name;
	}
	public String getIce_class() {
		return ice_class;
	}
	public void setIce_class(String ice_class) {
		this.ice_class = ice_class;
	}
	public float getSpeed() {
		return speed;
	}
	public void setSpeed(float speed) {
		this.speed = speed;
	}
	public float getLoad() {
		return load;
	}
	public void setLoad(float load) {
		this.load = load;
	}
	public float getLength() {
		return length;
	}
	public void setLength(float length) {
		this.length = length;
	}
	public float getWidth() {
		return width;
	}
	public void setWidth(float width) {
		this.width = width;
	}
	public float getDraft() {
		return draft;
	}
	public void setDraft(float draft) {
		this.draft = draft;
	}
	public float getD() {
		return D;
	}
	public void setD(float d) {
		D = d;
	}
	public String getCargo_types() {
		return cargo_types;
	}
	public void setCargo_types(String cargo_types) {
		this.cargo_types = cargo_types;
	}
	public float getDelta() {
		return delta;
	}
	public void setDelta(float delta) {
		this.delta = delta;
	}
	public float getCapacity() {
		return capacity;
	}
	public void setCapacity(float capacity) {
		this.capacity = capacity;
	}
	public float getBoardheight() {
		return boardheight;
	}
	public void setBoardheight(float boardheight) {
		this.boardheight = boardheight;
	}

}