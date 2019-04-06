package hu.mandisco.weddingScript.model.bean;

import java.util.Date;

public class Script {
	private int scriptId;
	private String name;
	private Date date;
	private String comment;
	private Date lastEdited;
	private Date created;

	public Script() {
		super();
	}

	public Script(String name, Date date, String comment) {
		super();
		this.name = name;
		this.date = date;
		this.comment = comment;
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

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Date getLastEdited() {
		return lastEdited;
	}

	public void setLastEdited(Date lastEdited) {
		this.lastEdited = lastEdited;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

}
