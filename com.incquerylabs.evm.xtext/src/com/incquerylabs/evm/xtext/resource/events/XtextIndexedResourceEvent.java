package com.incquerylabs.evm.xtext.resource.events;

import org.eclipse.incquery.runtime.evm.api.event.Event;
import org.eclipse.incquery.runtime.evm.api.event.EventType;
import org.eclipse.xtext.resource.IResourceDescription;

import com.incquerylabs.evm.xtext.XtextIndexEventType;
import com.incquerylabs.evm.xtext.resource.XtextIndexedResource;

public class XtextIndexedResourceEvent implements Event<XtextIndexedResource> {

	private final XtextIndexEventType type;
	private final XtextIndexedResource atom;
	
	protected XtextIndexedResourceEvent(XtextIndexEventType type, IResourceDescription atom) {
		this(type, new XtextIndexedResource(atom));
	}
	protected XtextIndexedResourceEvent(XtextIndexEventType type, XtextIndexedResource atom) {
		super();
		this.type = type;
		this.atom = atom;
	}

	@Override
	public EventType getEventType() {
		return type;
	}

	@Override
	public XtextIndexedResource getEventAtom() {
		return atom;
	}

}
