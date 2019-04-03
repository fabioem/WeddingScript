package hu.mandisco.weddingScript.controller;

import hu.mandisco.weddingScript.model.bean.Service;

public class ServiceController {

	Service model;

	public ServiceController(Service model) {
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

	// ServiceId
	public int getServiceId() {
		return model.getServiceId();
	}

	public void setServiceId(int serviceId) {
		model.setServiceId(serviceId);
	}

}
