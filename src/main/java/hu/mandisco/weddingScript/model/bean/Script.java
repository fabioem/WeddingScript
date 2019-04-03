package hu.mandisco.weddingScript.model.bean;

import java.sql.Date;

public class Script {
	private int scriptId;
	private String name;
	private Date date;

	public Script(int scriptId, String name, Date date) {
		super();
		this.scriptId = scriptId;
		this.name = name;
		this.date = date;
	}

	public Script() {
		super();
	}

	public int getScriptId() {
		return scriptId;
	}

	public void setScriptId(int scriptId) {
		this.scriptId = scriptId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
}
