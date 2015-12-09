package com.incquerylabs.evm.xtext.eobject.event;

import org.eclipse.incquery.runtime.evm.api.event.Event;
import org.eclipse.incquery.runtime.evm.api.event.EventType;
import org.eclipse.xtext.resource.IEObjectDescription;
import org.eclipse.xtext.resource.IResourceDescription;

import com.incquerylabs.evm.xtext.XtextIndexEventType;
import com.incquerylabs.evm.xtext.eobject.XtextIndexedObject;

public class XtextIndexedObjectEvent implements Event<XtextIndexedObject> {

	private final XtextIndexEventType type;
	private final XtextIndexedObject atom;
	
	protected XtextIndexedObjectEvent(XtextIndexEventType type, IResourceDescription resource, IEObjectDescription atom) {
		this(type, new XtextIndexedObject(resource, atom));
	}
	protected XtextIndexedObjectEvent(XtextIndexEventType type, XtextIndexedObject atom) {
		super();
		this.type = type;
		this.atom = atom;
	}

	@Override
	public EventType getEventType() {
		return type;
	}

	@Override
	public XtextIndexedObject getEventAtom() {
		return atom;
	}

}
