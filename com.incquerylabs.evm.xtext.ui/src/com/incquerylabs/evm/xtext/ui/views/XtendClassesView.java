package com.incquerylabs.evm.xtext.ui.views;

import javax.inject.Inject;

import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.xtext.common.types.TypesPackage;
import org.eclipse.xtext.resource.IResourceDescriptions;
import org.eclipse.xtext.ui.editor.IURIEditorOpener;
import org.eclipse.xtext.ui.notification.IStateChangeEventBroker;

import com.incquerylabs.evm.xtext.eobject.XtextIndexedObject;
import com.incquerylabs.evm.xtext.ui.XtextIndexLoggerApplication;

public class XtendClassesView extends ViewPart {

	@Inject
	private IURIEditorOpener uriOpener;
	
	@Inject
	public XtendClassesView(IStateChangeEventBroker source, IResourceDescriptions descriptions) {
		super();
		XtextIndexLoggerApplication.INSTANCE.initialize(source, descriptions);
	}

	@Override
	public void createPartControl(Composite parent) {
		parent.setLayout(new FillLayout());
		
		ListViewer viewer = new ListViewer(parent);
		viewer.setContentProvider(new XtendClassContentProvider());
		viewer.setLabelProvider(new XtextIndexedObjectLabelProvider());
		viewer.setInput(TypesPackage.Literals.JVM_TYPE);
		
		viewer.addDoubleClickListener(new IDoubleClickListener() {
			
			@Override
			public void doubleClick(DoubleClickEvent event) {
				IStructuredSelection selection = (IStructuredSelection) event.getSelection();
				if (selection.getFirstElement() instanceof XtextIndexedObject) {
					XtextIndexedObject object = (XtextIndexedObject) selection.getFirstElement();
					
					uriOpener.open(object.getEObject().getEObjectURI(), true);
				}
				
			}
		});
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

}
