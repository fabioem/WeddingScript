package hu.mandisco.weddingScript.view.edit;

import hu.mandisco.weddingScript.controller.WeddingScriptController;
import hu.mandisco.weddingScript.model.bean.Attribute;
import javafx.collections.ObservableList;

public class AttributeEditWindow {


	private static WeddingScriptController weddingScriptController = new WeddingScriptController();

	private ObservableList<Attribute> attributeItems;

	public AttributeEditWindow(ObservableList<Attribute> attributeItems) {
		this.attributeItems = attributeItems;
	}


	public void display(Attribute selectedItem) {
		// TODO AttributeEditWindow.display()

	}

}
