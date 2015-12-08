package com.incquerylabs.evm.xtext.resource;

import org.eclipse.emf.common.util.URI;
import org.eclipse.xtext.resource.IResourceDescription;

public class XtextIndexedResource {

	private final IResourceDescription resource;
	private final URI resourceURI;

	public XtextIndexedResource(IResourceDescription resource) {
		super();
		this.resource = resource;
		this.resourceURI = resource.getURI();
	}

	public IResourceDescription getResource() {
		return resource;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
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
		XtextIndexedResource other = (XtextIndexedResource) obj;
		if (resourceURI == null) {
			if (other.resourceURI != null)
				return false;
		} else if (!resourceURI.equals(other.resourceURI))
			return false;
		return true;
	}
	
	
}
