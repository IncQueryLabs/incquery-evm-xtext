package com.incquerylabs.evm.xtext.eobject;

import org.eclipse.xtext.resource.IEObjectDescription;
import org.eclipse.xtext.resource.IResourceDescription;

public interface IXtextObjectProcessor {

	void processObject(IResourceDescription resource, IEObjectDescription desc); 
}
