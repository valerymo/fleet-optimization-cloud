package com.shipdesign.restservice;

import java.util.List;

public class ShipDesignRequestObject {

	
	private String proj_name;
	private String proj_load;
	private String proj_speed;
	
    private String proto_name;
    private String proto_projname;
    private String proto_classification_society;
    private List<String>  proto_class_notations;
    private String proto_ice_class;
    private String proto_speed; 
    private String proto_load;
    private String proto_length;
    private String proto_width;
    private String proto_draft;
    private String proto_D;
    private List<String> proto_cargo_types;
    
	public String getProj_name() {
		return proj_name;
	}
	public void setProj_name(String proj_name) {
		this.proj_name = proj_name;
	}
	public String getProj_load() {
		return proj_load;
	}
	public void setProj_load(String proj_load) {
		this.proj_load = proj_load;
	}
	public String getProj_speed() {
		return proj_speed;
	}
	public void setProj_speed(String proj_speed) {
		this.proj_speed = proj_speed;
	}
	public String getProto_name() {
		return proto_name;
	}
	public void setProto_name(String proto_name) {
		this.proto_name = proto_name;
	}
	public String getProto_projname() {
		return proto_projname;
	}
	public void setProto_projname(String proto_projname) {
		this.proto_projname = proto_projname;
	}
	public String getProto_classification_society() {
		return proto_classification_society;
	}
	public void setProto_classification_society(String proto_classification_society) {
		this.proto_classification_society = proto_classification_society;
	}
	public List<String> getProto_class_notations() {
		return proto_class_notations;
	}
	public void setProto_class_notations(List<String> proto_class_notations) {
		this.proto_class_notations = proto_class_notations;
	}
	public String getProto_ice_class() {
		return proto_ice_class;
	}
	public void setProto_ice_class(String proto_ice_class) {
		this.proto_ice_class = proto_ice_class;
	}
	public String getProto_speed() {
		return proto_speed;
	}
	public void setProto_speed(String proto_speed) {
		this.proto_speed = proto_speed;
	}
	public String getProto_load() {
		return proto_load;
	}
	public void setProto_load(String proto_dwt) {
		this.proto_load = proto_dwt;
	}
	public String getProto_length() {
		return proto_length;
	}
	public void setProto_length(String proto_length) {
		this.proto_length = proto_length;
	}
	public String getProto_width() {
		return proto_width;
	}
	public void setProto_width(String proto_width) {
		this.proto_width = proto_width;
	}
	public String getProto_draft() {
		return proto_draft;
	}
	public void setProto_draft(String proto_draft) {
		this.proto_draft = proto_draft;
	}
	public List<String> getProto_cargo_types() {
		return proto_cargo_types;
	}
	public void setProto_cargo_types(List<String> proto_cargo_types) {
		this.proto_cargo_types = proto_cargo_types;
	}
	public String getProto_D() {
		return proto_D;
	}
	public void setProto_D(String proto_D) {
		this.proto_D = proto_D;
	}
	
}
