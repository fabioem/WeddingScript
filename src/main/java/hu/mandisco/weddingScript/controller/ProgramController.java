package hu.mandisco.weddingScript.controller;

import hu.mandisco.weddingScript.model.bean.Program;

public class ProgramController {
	Program model;

	public ProgramController(Program model) {
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

	// ProgId
	public int getProgId() {
		return model.getProgId();
	}

	public void setProgId(int progId) {
		model.setProgId(progId);
	}
}
