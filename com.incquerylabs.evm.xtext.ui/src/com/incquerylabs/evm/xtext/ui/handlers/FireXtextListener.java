package com.incquerylabs.evm.xtext.ui.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;

import com.incquerylabs.evm.xtext.ui.XtextIndexVirtualMachine;

public class FireXtextListener extends AbstractHandler implements IHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		XtextIndexVirtualMachine.INSTANCE.fire();
		return null;
	}

}
