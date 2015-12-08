package com.incquerylabs.evm.xtext.ui.handlers;

import javax.inject.Inject;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.xtext.builder.builderState.IBuilderState;
import org.eclipse.xtext.resource.IResourceDescription.Event;
import org.eclipse.xtext.resource.IResourceDescription.Event.Listener;

import com.google.inject.Injector;
import com.incquerylabs.evm.xtext.ui.XtextIndexLoggerApplication;

public class InitializeXtextListener extends AbstractHandler implements IHandler {

	@Inject
	IBuilderState state;
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		XtextIndexLoggerApplication.INSTANCE.initialize(state);
		return null;
	}

}
