package com.incquerylabs.evm.xtext.resource;

import org.eclipse.incquery.runtime.evm.api.Activation;
import org.eclipse.incquery.runtime.evm.api.Context;
import org.eclipse.incquery.runtime.evm.api.Job;
import org.eclipse.incquery.runtime.evm.api.event.ActivationState;

public class XtextResourceJob extends Job<XtextIndexedResource>{

	private IXtextResourceProcessor resourceProcessor;

	public XtextResourceJob(ActivationState activationState, IXtextResourceProcessor resourceProcessor) {
		super(activationState);
		this.resourceProcessor = resourceProcessor;
	}

	@Override
	protected void execute(Activation<? extends XtextIndexedResource> activation, Context context) {
		resourceProcessor.processResource(activation.getAtom().getResource());
	}
	
	@Override
	protected void handleError(Activation<? extends XtextIndexedResource> activation, Exception exception,
			Context context) {
	}


}
