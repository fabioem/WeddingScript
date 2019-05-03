package hu.mandisco.weddingScript.model.bean;

import java.time.LocalDateTime;

import javafx.collections.ObservableList;

public class Program {
	private int progId;
	private String name;
	private LocalDateTime defaultTime;
	private LocalDateTime time;
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

}
