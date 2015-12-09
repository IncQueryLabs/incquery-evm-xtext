package com.incquerylabs.evm.xtext;

import org.eclipse.incquery.runtime.evm.api.event.EventRealm;
import org.eclipse.xtext.resource.IResourceDescription.Event;
import org.eclipse.xtext.resource.IResourceDescriptions;

public class XtextEventRealm implements EventRealm {

	final Event.Source eventSource;
	final IResourceDescriptions descriptions;

	public XtextEventRealm(Event.Source eventSource, IResourceDescriptions descriptions) {
		super();
		this.eventSource = eventSource;
		this.descriptions = descriptions;
	}

	public Event.Source getEventSource() {
		return eventSource;
	}

	public IResourceDescriptions getDescriptions() {
		return descriptions;
	}

	public void dispose() {
		
	}
}
