package hu.mandisco.weddingscript.model.bean;

import java.time.LocalDateTime;

import javafx.collections.ObservableList;

public class Program implements Comparable<Program>{
	private int progId;
	private String name;
	private LocalDateTime defaultTime;
	private LocalDateTime time;
	private Boolean defaultProgram;
	ObservableList<Attribute> attributes;

	public ObservableList<Attribute> getAttributes() {
		return attributes;
	}

	public void setAttributes(ObservableList<Attribute> attributes) {
		this.attributes = attributes;
	}

	public int getProgId() {
		return progId;
	}

	public void setProgId(int progId) {
		this.progId = progId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LocalDateTime getDefaultTime() {
		return defaultTime;
	}

	public void setDefaultTime(LocalDateTime defaultTime) {
		this.defaultTime = defaultTime;
	}

	public LocalDateTime getTime() {
		return time;
	}

	public void setTime(LocalDateTime time) {
		this.time = time;
	}

	public Boolean isDefaultProgram() {
		return defaultProgram;
	}

	public void setDefaultProgram(boolean defaultProgram) {
		this.defaultProgram = defaultProgram;
	}
	
	@Override
	public int compareTo(Program p) {
	    return getTime().compareTo(p.getTime());
	}

}
