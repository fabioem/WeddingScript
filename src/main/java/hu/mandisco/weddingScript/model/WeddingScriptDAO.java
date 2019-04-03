package hu.mandisco.weddingScript.model;

import java.util.List;

import hu.mandisco.weddingScript.model.bean.Program;
import hu.mandisco.weddingScript.model.bean.Script;

public interface WeddingScriptDAO {

	public List<Program> getPrograms();
	public List<Script> getScripts();

}
