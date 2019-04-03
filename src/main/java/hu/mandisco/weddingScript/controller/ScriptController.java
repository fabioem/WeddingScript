package hu.mandisco.weddingScript.controller;

import hu.mandisco.weddingScript.model.bean.Script;

public class ScriptController {
	private Script model;

	public ScriptController(Script model) {
		super();
		this.model = model;
	}

	// Name
	public String getName() {
		return model.getName();
	}

	public void setName(String name) {
		model.setName(name);
	}

	//ScriptId
	public int getScriptId() {
		return model.getScriptId();
	}

	public void setScriptId(int scriptId){
		model.setScriptId(scriptId);
	}
}
