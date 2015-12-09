package com.incquerylabs.evm.xtext.eobject;

import org.eclipse.incquery.runtime.evm.api.Activation;
import org.eclipse.incquery.runtime.evm.api.Context;
import org.eclipse.incquery.runtime.evm.api.Job;
import org.eclipse.incquery.runtime.evm.api.event.ActivationState;

public class XtextObjectJob extends Job<XtextIndexedObject>{

	private IXtextObjectProcessor resourceProcessor;

	public XtextObjectJob(ActivationState activationState, IXtextObjectProcessor resourceProcessor) {
		super(activationState);
		this.resourceProcessor = resourceProcessor;
	}

	@Override
	protected void execute(Activation<? extends XtextIndexedObject> activation, Context context) {
		XtextIndexedObject atom = activation.getAtom();
		resourceProcessor.processObject(atom.getResource(), atom.getEObject());
	}
	
	@Override
	protected void handleError(Activation<? extends XtextIndexedObject> activation, Exception exception,
			Context context) {
	}


}
