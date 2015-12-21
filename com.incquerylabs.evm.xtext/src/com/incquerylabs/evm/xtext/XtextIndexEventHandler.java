package com.incquerylabs.evm.xtext;

import java.util.Map;

import org.eclipse.incquery.runtime.evm.api.Activation;
import org.eclipse.incquery.runtime.evm.api.RuleInstance;
import org.eclipse.incquery.runtime.evm.api.event.Event;
import org.eclipse.incquery.runtime.evm.api.event.EventFilter;
import org.eclipse.incquery.runtime.evm.api.event.EventType;
import org.eclipse.incquery.runtime.evm.api.event.adapter.EventHandlerAdapter;
import org.eclipse.incquery.runtime.evm.api.event.adapter.EventProcessorAdapter;
import org.eclipse.incquery.runtime.evm.api.event.adapter.EventSourceAdapter;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;

public class XtextIndexEventHandler<Atom, Source extends EventSourceAdapter<Atom>> extends EventHandlerAdapter<Atom> {
	
	public XtextIndexEventHandler(Source source, EventFilter<? super Atom> filter,
			 RuleInstance<Atom> ruleInstance) {
		super(source, filter, ruleInstance);
	}

	@Override
	public void prepareEventHandler() {
		super.prepareEventHandler();
		
		((EventSourceAdapter<Atom>)getSource()).addHandler(this);
	}

	@Override
	protected Map<EventType, EventProcessorAdapter<Atom>> prepareEventProcessors() {
		Map<EventType, EventProcessorAdapter<Atom>> processors = Maps.newHashMap();
		processors.put(XtextIndexEventType.CREATED, new EventProcessorAdapter<Atom>(getInstance()) {
			
			@Override
			protected void activationExists(Event<Atom> event, Activation<Atom> activation) {
				getInstance().activationStateTransition(activation, XtextIndexEventType.CREATED);
			}
			
			@Override
			protected void activationMissing(Event<Atom> event) {
				Atom eventAtom = event.getEventAtom();
				Activation<Atom> activation = getInstance().createActivation(eventAtom);
				getInstance().activationStateTransition(activation, XtextIndexEventType.CREATED);
			}
			
		});
		
		processors.put(XtextIndexEventType.UPDATED, new EventProcessorAdapter<Atom>(getInstance()) {

			@Override
			protected void activationExists(Event<Atom> event,
					Activation<Atom> activation) {
				getInstance().activationStateTransition(activation, XtextIndexEventType.UPDATED);
				
			}

			@Override
			protected void activationMissing(Event<Atom> event) {
//				Preconditions.checkState(false, "Atom %s updated without existing activation in rule instance %s", event.getEventAtom(), this);
			}
		});
		
		processors.put(XtextIndexEventType.REMOVED, new EventProcessorAdapter<Atom>(getInstance()) {

			@Override
			protected void activationExists(Event<Atom> event,
					Activation<Atom> activation) {
				getInstance().activationStateTransition(activation, XtextIndexEventType.REMOVED);
			}

			@Override
			protected void activationMissing(Event<Atom> event) {
//				Preconditions.checkState(false, "Atom %s disappeared without existing activation in rule instance %s!", event.getEventAtom(), this);
			}
		});
		
		return processors;
	}

	@Override
	public void dispose() {
		((EventSourceAdapter<Atom>)getSource()).removeHandler(this);
	}

}
