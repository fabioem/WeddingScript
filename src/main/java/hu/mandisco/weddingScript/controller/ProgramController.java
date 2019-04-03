package hu.mandisco.weddingScript.controller;

import hu.mandisco.weddingScript.model.bean.Program;
import hu.mandisco.weddingScript.view.ProgramView;

public class ProgramController {
	Program model;
	ProgramView view;

	public ProgramController(Program model, ProgramView view) {
		super();
		this.model = model;
		this.view = view;
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
