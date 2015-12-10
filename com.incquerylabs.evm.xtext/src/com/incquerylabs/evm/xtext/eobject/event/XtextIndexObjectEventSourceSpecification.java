package com.incquerylabs.evm.xtext.eobject.event;

import org.eclipse.incquery.runtime.evm.api.RuleInstance;
import org.eclipse.incquery.runtime.evm.api.event.AbstractRuleInstanceBuilder;
import org.eclipse.incquery.runtime.evm.api.event.EventFilter;
import org.eclipse.incquery.runtime.evm.api.event.EventRealm;
import org.eclipse.incquery.runtime.evm.api.event.EventSourceSpecification;

import com.google.common.base.Preconditions;
import com.incquerylabs.evm.xtext.XtextEventRealm;
import com.incquerylabs.evm.xtext.XtextIndexEventHandler;
import com.incquerylabs.evm.xtext.eobject.XtextIndexedObject;

public class XtextIndexObjectEventSourceSpecification implements EventSourceSpecification<XtextIndexedObject> {

	@Override
	public EventFilter<XtextIndexedObject> createEmptyFilter() {
		return new EventFilter<XtextIndexedObject>() {

			@Override
			public boolean isProcessable(XtextIndexedObject eventAtom) {
				return true;
			}
		};
	}

	@Override
	public AbstractRuleInstanceBuilder<XtextIndexedObject> getRuleInstanceBuilder(final EventRealm realm) {
		Preconditions.checkArgument(realm instanceof XtextEventRealm, "Event realm must be an XtextEventRealm");
		return new AbstractRuleInstanceBuilder<XtextIndexedObject>() {

			@Override
			public void prepareRuleInstance(RuleInstance<XtextIndexedObject> ruleInstance,
					EventFilter<? super XtextIndexedObject> filter) {
				XtextIndexedObjectEventSource source = new XtextIndexedObjectEventSource(
						XtextIndexObjectEventSourceSpecification.this, (XtextEventRealm) realm);
				source.prepareSource();
				XtextIndexEventHandler<XtextIndexedObject, XtextIndexedObjectEventSource> handler = new XtextIndexEventHandler<>(
						source, filter, ruleInstance);
				handler.prepareEventHandler();
			}
		};
	}

}
