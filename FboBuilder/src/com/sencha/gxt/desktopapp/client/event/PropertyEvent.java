package com.sencha.gxt.desktopapp.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.sencha.gxt.desktopapp.client.event.PropertyEvent.PropertyHandler;

public class PropertyEvent extends GwtEvent<PropertyHandler> {

	public interface PropertyHandler extends EventHandler {
		void onProperty(PropertyEvent event);
	}

	public static Type<PropertyHandler> TYPE = new Type<PropertyHandler>();
	private Object bean;

	public PropertyEvent(Object bean) {
		this.bean = bean;
	}

	@Override
	public Type<PropertyHandler> getAssociatedType() {
		return TYPE;
	}
	
	public Object getBean() {
		return bean;
	}

	@Override
	protected void dispatch(PropertyHandler handler) {
		handler.onProperty(this);
	}
	
	@Override
	public String toString() {
	    return bean.toString();
	}
}