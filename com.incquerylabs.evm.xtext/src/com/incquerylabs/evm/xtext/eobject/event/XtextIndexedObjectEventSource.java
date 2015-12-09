package com.incquerylabs.evm.xtext.eobject.event;

import org.eclipse.incquery.runtime.evm.api.event.EventHandler;
import org.eclipse.incquery.runtime.evm.api.event.EventSourceSpecification;
import org.eclipse.incquery.runtime.evm.api.event.adapter.EventSourceAdapter;
import org.eclipse.xtext.resource.IEObjectDescription;
import org.eclipse.xtext.resource.IResourceDescription;
import org.eclipse.xtext.resource.IResourceDescription.Delta;
import org.eclipse.xtext.resource.IResourceDescription.Event;
import org.eclipse.xtext.resource.IResourceDescription.Event.Listener;

import com.incquerylabs.evm.xtext.XtextEventRealm;
import com.incquerylabs.evm.xtext.XtextIndexEventType;
import com.incquerylabs.evm.xtext.eobject.XtextIndexedObject;

public class XtextIndexedObjectEventSource extends EventSourceAdapter<XtextIndexedObject>{

	private final class XtextResourceDescriptionChangeListener implements Listener {
		@Override
		public void descriptionsChanged(Event event) {
			for (Delta delta : event.getDeltas()) {
				if (delta.haveEObjectDescriptionsChanged()) {
					if (delta.getOld() != null) {
						//Remove event
						for (IEObjectDescription description : delta.getOld().getExportedObjects()) {
							notifyHandlers(new XtextIndexedObjectEvent(XtextIndexEventType.REMOVED, delta.getOld(), description));
						}
					}
					if (delta.getNew() != null) {
						//Addition event
						for (IEObjectDescription description : delta.getNew().getExportedObjects()) {
							notifyHandlers(new XtextIndexedObjectEvent(XtextIndexEventType.CREATED, delta.getNew(), description));
						}
					}
//					if (delta.getOld() == null && delta.getNew() != null) {
//						//Addition event
//						for (IEObjectDescription description : delta.getNew().getExportedObjects()) {
//							notifyHandlers(new XtextIndexedEObjectEvent(XtextIndexEventType.CREATED, delta.getNew(), description));
//						}
//					} else if (delta.getOld() != null && delta.getNew() == null) {
//						//Remove event
//						for (IEObjectDescription description : delta.getOld().getExportedObjects()) {
//							notifyHandlers(new XtextIndexedEObjectEvent(XtextIndexEventType.REMOVED, delta.getOld(), description));
//						}
//					} else if (delta.getOld() != null && delta.getNew() != null) {
//						//Update event
//						//notifyHandlers(new XtextIndexedEObjectEvent(XtextIndexEventType.UPDATED, delta.getNew()));
//						//TODO
//					}
				}
			}
		}
	}

	protected void resendEventsForExistingResources(EventHandler<XtextIndexedObject> handler) {
		for (IResourceDescription description : realm.getDescriptions().getAllResourceDescriptions()) {
			for (IEObjectDescription objectDescription : description.getExportedObjects()) {
				handler.handleEvent(new XtextIndexedObjectEvent(XtextIndexEventType.CREATED, description, objectDescription));
			}
		}
	}
	
	@Override
	protected void beforeHandlerAdded(EventHandler<XtextIndexedObject> handler, boolean handlersEmpty) {
		super.beforeHandlerAdded(handler, handlersEmpty);
		resendEventsForExistingResources(handler);
	}

	private XtextEventRealm realm;
	private XtextResourceDescriptionChangeListener resourceListener;

	public XtextIndexedObjectEventSource(EventSourceSpecification<XtextIndexedObject> specification,
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
