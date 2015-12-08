package com.incquerylabs.evm.xtext;

import org.eclipse.incquery.runtime.evm.api.event.EventRealm;
import org.eclipse.xtext.builder.builderState.IBuilderState;
import org.eclipse.xtext.resource.IResourceDescription.Delta;
import org.eclipse.xtext.resource.IResourceDescription.Event;
import org.eclipse.xtext.resource.IResourceDescription.Event.Listener;

public class XtextEventRealm implements EventRealm {

	IBuilderState builderState;
	
	public XtextEventRealm(IBuilderState builderState) {
		this.builderState = builderState;
	}
	
	public IBuilderState getBuilderState() {
		return builderState;
	}

	public void dispose() {
		
	}
}
