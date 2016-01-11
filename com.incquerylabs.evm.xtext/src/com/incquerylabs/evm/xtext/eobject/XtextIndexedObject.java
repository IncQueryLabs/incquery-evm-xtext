package com.incquerylabs.evm.xtext.eobject;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.xtext.naming.QualifiedName;
import org.eclipse.xtext.resource.IEObjectDescription;
import org.eclipse.xtext.resource.IResourceDescription;

/**
 * This class represents a model index change event inside the EVM
 * @author Zoltan Ujhelyi
 *
 */
public class XtextIndexedObject {

	private final IResourceDescription resource;
	private final URI resourceURI;
	
	private final IEObjectDescription eObject;
	private final QualifiedName objectName;
	

	public XtextIndexedObject(IResourceDescription resource, IEObjectDescription eObject) {
		super();
		this.resource = resource;
		resourceURI = resource.getURI();
		this.eObject = eObject;
		objectName = eObject.getQualifiedName();
	}


	public IResourceDescription getResource() {
		return resource;
	}


	public IEObjectDescription getEObject() {
		return eObject;
	}


	public EClass getType() {
		return eObject.getEClass();
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((objectName == null) ? 0 : objectName.hashCode());
		result = prime * result + ((resourceURI == null) ? 0 : resourceURI.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		XtextIndexedObject other = (XtextIndexedObject) obj;
		if (objectName == null) {
			if (other.objectName != null)
				return false;
		} else if (!objectName.equals(other.objectName))
			return false;
		if (resourceURI == null) {
			if (other.resourceURI != null)
				return false;
		} else if (!resourceURI.equals(other.resourceURI))
			return false;
		return true;
	}


	@Override
	public String toString() {
		return eObject.getEClass().getName() + "(" + objectName + " in " + resourceURI.toString() + ")"; 
	}
}
