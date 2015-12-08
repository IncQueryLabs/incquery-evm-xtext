package com.incquerylabs.evm.xtext.resource.events;

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
import com.incquerylabs.evm.xtext.resource.XtextIndexedResource;

public class XtextIndexedResourceEventHandler extends EventHandlerAdapter<XtextIndexedResource> {

	
	public XtextIndexedResourceEventHandler(XtextIndexedResourceEventSource source, EventFilter<? super XtextIndexedResource> filter,
			 RuleInstance<XtextIndexedResource> ruleInstance) {
		super(source, filter, ruleInstance);
	}

	@Override
	protected void prepareEventHandler() {
		super.prepareEventHandler();
		
		((XtextIndexedResourceEventSource)getSource()).addHandler(this);
	}

	@Override
	protected Map<EventType, EventProcessorAdapter<XtextIndexedResource>> prepareEventProcessors() {
		Map<EventType, EventProcessorAdapter<XtextIndexedResource>> processors = Maps.newHashMap();
		processors.put(XtextIndexEventType.CREATED, new EventProcessorAdapter<XtextIndexedResource>(getInstance()) {
			
			@Override
			protected void activationExists(Event<XtextIndexedResource> event, Activation<XtextIndexedResource> activation) {
				getInstance().activationStateTransition(activation, XtextIndexEventType.CREATED);
			}
			
			@Override
			protected void activationMissing(Event<XtextIndexedResource> event) {
				XtextIndexedResource eventAtom = event.getEventAtom();
				Activation<XtextIndexedResource> activation = getInstance().createActivation(eventAtom);
				getInstance().activationStateTransition(activation, XtextIndexEventType.CREATED);
			}
			
		});
		
		processors.put(XtextIndexEventType.UPDATED, new EventProcessorAdapter<XtextIndexedResource>(getInstance()) {

			@Override
			protected void activationExists(Event<XtextIndexedResource> event,
					Activation<XtextIndexedResource> activation) {
				getInstance().activationStateTransition(activation, XtextIndexEventType.UPDATED);
				
			}

			@Override
			protected void activationMissing(Event<XtextIndexedResource> event) {
				Preconditions.checkState(false, "Resource %s updated without existing activation in rule instance %s", event.getEventAtom().getResource().getURI(), this);
			}
		});
		
		processors.put(XtextIndexEventType.REMOVED, new EventProcessorAdapter<XtextIndexedResource>(getInstance()) {

			@Override
			protected void activationExists(Event<XtextIndexedResource> event,
					Activation<XtextIndexedResource> activation) {
				getInstance().activationStateTransition(activation, XtextIndexEventType.REMOVED);
			}

			@Override
			protected void activationMissing(Event<XtextIndexedResource> event) {
				Preconditions.checkState(false, "Resource %s disappeared without existing activation in rule instance %s!", event.getEventAtom().getResource().getURI(), this);
			}
		});
		
		return processors;
	}

	@Override
	public void dispose() {
		((XtextIndexedResourceEventSource)getSource()).removeHandler(this);
	}

}
