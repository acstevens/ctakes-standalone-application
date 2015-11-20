package com.sentimetrix.ctakes.application.model;

import java.io.Serializable;
import java.util.Collection;

public class Entity implements Serializable{
	private static final long serialVersionUID = 1L;

	String name;
	String type;
	String subject;
	Integer polarity;
	Boolean uncertain;
	Boolean conditional;
	Boolean generic;
	Boolean historyOf;
	Collection<String> cuis;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public Integer getPolarity() {
		return polarity;
	}
	public void setPolarity(Integer polarity) {
		this.polarity = polarity;
	}
	public Boolean getUncertain() {
		return uncertain;
	}
	public void setUncertain(Boolean uncertain) {
		this.uncertain = uncertain;
	}
	public Boolean getConditional() {
		return conditional;
	}
	public void setConditional(Boolean conditional) {
		this.conditional = conditional;
	}
	public Boolean getGeneric() {
		return generic;
	}
	public void setGeneric(Boolean generic) {
		this.generic = generic;
	}
	public Boolean getHistoryOf() {
		return historyOf;
	}
	public void setHistoryOf(Boolean historyOf) {
		this.historyOf = historyOf;
	}
	public Collection<String> getCuis() {
		return cuis;
	}
	public void setCuis(Collection<String> cuis) {
		this.cuis = cuis;
	}
	
	@Override 
	public String toString() {
		StringBuilder result = new StringBuilder();
		String NEW_LINE = "\n";
		
		result.append(this.getClass().getName() + " Object {" + NEW_LINE);
		result.append(" conditional: " + conditional + NEW_LINE);
		result.append(" cuis: " + cuis + NEW_LINE );
		result.append(" generic: " + generic + NEW_LINE );
		result.append(" historyOf: " + historyOf + NEW_LINE );
		result.append(" name: " + name + NEW_LINE );
		result.append(" polarity: " + polarity + NEW_LINE );
		result.append(" subject: " + subject + NEW_LINE );
		result.append(" type: " + type + NEW_LINE );
		result.append(" uncertain: " + uncertain + NEW_LINE );
		result.append("}");

		return result.toString();
	}

}
