package hu.mandisco.weddingScript.model.bean;

import java.time.LocalDateTime;

public class Script {
	private int scriptId;
	private String name;
	private LocalDateTime date;
	private String comment;
	private LocalDateTime lastEdited;
	private LocalDateTime created;

	public Script() {
		super();
	}

	public Script(String name, LocalDateTime date, String comment) {
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

	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public LocalDateTime getLastEdited() {
		return lastEdited;
	}

	public void setLastEdited(LocalDateTime lastEdited) {
		this.lastEdited = lastEdited;
	}

	public LocalDateTime getCreated() {
		return created;
	}

	public void setCreated(LocalDateTime created) {
		this.created = created;
	}

}
