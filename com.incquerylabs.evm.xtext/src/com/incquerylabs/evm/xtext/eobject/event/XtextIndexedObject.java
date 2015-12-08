package com.incquerylabs.evm.xtext.eobject.event;

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
	
	private final EClass type;


	private XtextIndexedObject(IResourceDescription resource, IEObjectDescription eObject, EClass type) {
		super();
		this.resource = resource;
		resourceURI = resource.getURI();
		this.eObject = eObject;
		objectName = eObject.getQualifiedName();
		this.type = type;
	}


	public IResourceDescription getResource() {
		return resource;
	}


	public IEObjectDescription geteObject() {
		return eObject;
	}


	public EClass getType() {
		return type;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((objectName == null) ? 0 : objectName.hashCode());
		result = prime * result + ((resourceURI == null) ? 0 : resourceURI.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
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
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}
}
