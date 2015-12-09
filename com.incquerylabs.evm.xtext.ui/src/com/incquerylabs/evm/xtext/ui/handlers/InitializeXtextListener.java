package com.incquerylabs.evm.xtext.ui.handlers;

import javax.inject.Inject;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.xtext.resource.IResourceDescriptions;
import org.eclipse.xtext.ui.notification.IStateChangeEventBroker;

import com.incquerylabs.evm.xtext.ui.XtextIndexLoggerApplication;

public class InitializeXtextListener extends AbstractHandler implements IHandler {

	@Inject
	IStateChangeEventBroker source;
	@Inject
	IResourceDescriptions descriptions;
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		XtextIndexLoggerApplication.INSTANCE.initialize(source, descriptions);
		return null;
	}

}
