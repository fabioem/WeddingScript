package hu.mandisco.weddingScript.model.bean;

import java.time.LocalDateTime;

public class Program {
	private int progId;
	private String name;
	private LocalDateTime defaultTime;


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

}
