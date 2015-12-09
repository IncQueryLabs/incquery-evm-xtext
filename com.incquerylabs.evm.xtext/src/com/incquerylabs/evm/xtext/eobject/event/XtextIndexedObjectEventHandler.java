package com.incquerylabs.evm.xtext.eobject.event;

import java.util.Map;

import org.eclipse.incquery.runtime.evm.api.Activation;
import org.eclipse.incquery.runtime.evm.api.RuleInstance;
import org.eclipse.incquery.runtime.evm.api.event.Event;
import org.eclipse.incquery.runtime.evm.api.event.EventFilter;
import org.eclipse.incquery.runtime.evm.api.event.EventType;
import org.eclipse.incquery.runtime.evm.api.event.adapter.EventHandlerAdapter;
import org.eclipse.incquery.runtime.evm.api.event.adapter.EventProcessorAdapter;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.incquerylabs.evm.xtext.XtextIndexEventType;
import com.incquerylabs.evm.xtext.eobject.XtextIndexedObject;

public class XtextIndexedObjectEventHandler extends EventHandlerAdapter<XtextIndexedObject> {

	
	public XtextIndexedObjectEventHandler(XtextIndexedObjectEventSource source, EventFilter<? super XtextIndexedObject> filter,
			 RuleInstance<XtextIndexedObject> ruleInstance) {
		super(source, filter, ruleInstance);
	}

	@Override
	protected void prepareEventHandler() {
		super.prepareEventHandler();
		
		((XtextIndexedObjectEventSource)getSource()).addHandler(this);
	}

	@Override
	protected Map<EventType, EventProcessorAdapter<XtextIndexedObject>> prepareEventProcessors() {
		Map<EventType, EventProcessorAdapter<XtextIndexedObject>> processors = Maps.newHashMap();
		processors.put(XtextIndexEventType.CREATED, new EventProcessorAdapter<XtextIndexedObject>(getInstance()) {
			
			@Override
			protected void activationExists(Event<XtextIndexedObject> event, Activation<XtextIndexedObject> activation) {
				getInstance().activationStateTransition(activation, XtextIndexEventType.CREATED);
			}
			
			@Override
			protected void activationMissing(Event<XtextIndexedObject> event) {
				XtextIndexedObject eventAtom = event.getEventAtom();
				Activation<XtextIndexedObject> activation = getInstance().createActivation(eventAtom);
				getInstance().activationStateTransition(activation, XtextIndexEventType.CREATED);
			}
			
		});
		
		processors.put(XtextIndexEventType.UPDATED, new EventProcessorAdapter<XtextIndexedObject>(getInstance()) {

			@Override
			protected void activationExists(Event<XtextIndexedObject> event,
					Activation<XtextIndexedObject> activation) {
				getInstance().activationStateTransition(activation, XtextIndexEventType.UPDATED);
				
			}

			@Override
			protected void activationMissing(Event<XtextIndexedObject> event) {
				Preconditions.checkState(false, "Resource %s updated without existing activation in rule instance %s", event.getEventAtom().getResource().getURI(), this);
			}
		});
		
		processors.put(XtextIndexEventType.REMOVED, new EventProcessorAdapter<XtextIndexedObject>(getInstance()) {

			@Override
			protected void activationExists(Event<XtextIndexedObject> event,
					Activation<XtextIndexedObject> activation) {
				getInstance().activationStateTransition(activation, XtextIndexEventType.REMOVED);
			}

			@Override
			protected void activationMissing(Event<XtextIndexedObject> event) {
				Preconditions.checkState(false, "Resource %s disappeared without existing activation in rule instance %s!", event.getEventAtom().getResource().getURI(), this);
			}
		});
		
		return processors;
	}

	@Override
	public void dispose() {
		((XtextIndexedObjectEventSource)getSource()).removeHandler(this);
	}

}
