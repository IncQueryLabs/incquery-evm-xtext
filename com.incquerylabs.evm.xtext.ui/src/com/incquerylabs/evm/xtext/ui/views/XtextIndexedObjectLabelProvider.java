package com.incquerylabs.evm.xtext.ui.views;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.xtext.resource.IEObjectDescription;

import com.incquerylabs.evm.xtext.eobject.XtextIndexedObject;

public class XtextIndexedObjectLabelProvider extends LabelProvider {

	@Override
	public String getText(Object element) {
		if (element instanceof XtextIndexedObject) {
			XtextIndexedObject indexedObject = (XtextIndexedObject) element;
			IEObjectDescription description = indexedObject.getEObject();
			return String.format("%s %s (%s)", description.getEClass().getName(), description.getName(),
					indexedObject.getResource().getURI());
		}
		return super.getText(element);
	}

}
