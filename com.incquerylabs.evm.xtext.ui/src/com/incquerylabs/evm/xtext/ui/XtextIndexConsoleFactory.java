package com.incquerylabs.evm.xtext.ui;

import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleFactory;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.xtext.resource.IResourceDescriptions;
import org.eclipse.xtext.ui.notification.IStateChangeEventBroker;

import com.google.inject.Inject;

public class XtextIndexConsoleFactory implements IConsoleFactory {
	private final IStateChangeEventBroker source;
	private final IResourceDescriptions descriptions;
	
	@Inject
	public XtextIndexConsoleFactory(IStateChangeEventBroker source, IResourceDescriptions descriptions) {
		super();
		this.source = source;
		this.descriptions = descriptions;
	}
	
	
	@Override
	public void openConsole() {
		IConsoleManager manager = ConsolePlugin.getDefault().getConsoleManager();
		MessageConsole console = new XtextIndexMessageConsole(source, descriptions);
		manager.addConsoles(new IConsole[] {console});
		manager.showConsoleView(console);
	}

}
