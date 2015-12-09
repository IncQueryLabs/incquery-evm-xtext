package com.incquerylabs.evm.xtext.resource.events;

import org.eclipse.incquery.runtime.evm.api.event.EventHandler;
import org.eclipse.incquery.runtime.evm.api.event.EventSourceSpecification;
import org.eclipse.incquery.runtime.evm.api.event.adapter.EventSourceAdapter;
import org.eclipse.xtext.resource.IResourceDescription;
import org.eclipse.xtext.resource.IResourceDescription.Delta;
import org.eclipse.xtext.resource.IResourceDescription.Event;
import org.eclipse.xtext.resource.IResourceDescription.Event.Listener;

import com.incquerylabs.evm.xtext.XtextEventRealm;
import com.incquerylabs.evm.xtext.XtextIndexEventType;
import com.incquerylabs.evm.xtext.resource.XtextIndexedResource;

public class XtextIndexedResourceEventSource extends EventSourceAdapter<XtextIndexedResource>{

	private final class XtextResourceDescriptionChangeListener implements Listener {
		@Override
		public void descriptionsChanged(Event event) {
			for (Delta delta : event.getDeltas()) {
				if (delta.getOld() == null && delta.getNew() != null) {
					// Addition event
					notifyHandlers(new XtextIndexedResourceEvent(XtextIndexEventType.CREATED, delta.getNew()));
				} else if (delta.getOld() != null && delta.getNew() == null) {
					// Remove event
					notifyHandlers(new XtextIndexedResourceEvent(XtextIndexEventType.REMOVED, delta.getOld()));
				} else if (delta.getOld() != null && delta.getNew() != null) {
					// Update event
					notifyHandlers(new XtextIndexedResourceEvent(XtextIndexEventType.UPDATED, delta.getNew()));
				}
			}
		}
	}

	protected void resendEventsForExistingResources(EventHandler<XtextIndexedResource> handler) {
		for (IResourceDescription description : realm.getDescriptions().getAllResourceDescriptions()) {
			handler.handleEvent(new XtextIndexedResourceEvent(XtextIndexEventType.CREATED, description));
		}
	}
	
	@Override
	protected void beforeHandlerAdded(EventHandler<XtextIndexedResource> handler, boolean handlersEmpty) {
		super.beforeHandlerAdded(handler, handlersEmpty);
		resendEventsForExistingResources(handler);
	}

	private XtextEventRealm realm;
	private XtextResourceDescriptionChangeListener resourceListener;

	public XtextIndexedResourceEventSource(EventSourceSpecification<XtextIndexedResource> specification,
			XtextEventRealm realm) {
		super(specification, realm);
		this.realm = realm;
	}

	@Override
	protected void prepareSource() {
		resourceListener = new XtextResourceDescriptionChangeListener();
		realm.getEventSource().addListener(resourceListener);
		
	}

	@Override
	public void dispose() {
		if (resourceListener != null) {
			realm.getEventSource().removeListener(resourceListener);
			resourceListener = null;
		}
		super.dispose();
	}

}
