package com.incquerylabs.evm.xtext.ui.views;

import javax.inject.Inject;

import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.xtext.common.types.TypesPackage;
import org.eclipse.xtext.resource.IResourceDescriptions;
import org.eclipse.xtext.ui.editor.IURIEditorOpener;
import org.eclipse.xtext.ui.notification.IStateChangeEventBroker;

import com.incquerylabs.evm.xtext.eobject.XtextIndexedObject;
import com.incquerylabs.evm.xtext.ui.XtextIndexVirtualMachine;

public class XtendClassesView extends ViewPart {

	@Inject
	private IURIEditorOpener uriOpener;
	
	@Inject
	public XtendClassesView(IStateChangeEventBroker source, IResourceDescriptions descriptions) {
		super();
		XtextIndexVirtualMachine.INSTANCE.initialize(source, descriptions);
	}

	@Override
	public void createPartControl(Composite parent) {
		TableColumnLayout columnLayout = new TableColumnLayout();
		parent.setLayout(columnLayout);
		
		TableViewer viewer = new TableViewer(parent);
		viewer.setContentProvider(new XtendClassContentProvider());
		
		TableViewerColumn column = new TableViewerColumn(viewer, SWT.NONE);
		XtextIndexedObjectLabelProvider labelProvider = new XtextIndexedObjectLabelProvider();
		column.setLabelProvider(labelProvider);
		ColumnViewerToolTipSupport.enableFor(viewer);
		
		columnLayout.setColumnData(column.getColumn(), new ColumnWeightData(100));
		
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
		
		viewer.setInput(TypesPackage.Literals.JVM_TYPE);
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

}
