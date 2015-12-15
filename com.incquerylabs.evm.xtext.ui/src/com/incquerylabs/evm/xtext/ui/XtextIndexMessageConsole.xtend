package com.incquerylabs.evm.xtext.ui

import com.incquerylabs.evm.xtext.XtextIndexActivationState
import com.incquerylabs.evm.xtext.XtextIndexRules
import org.eclipse.swt.SWT
import org.eclipse.ui.console.ConsolePlugin
import org.eclipse.ui.console.MessageConsole
import org.eclipse.xtext.resource.IResourceDescriptions
import org.eclipse.xtext.ui.notification.IStateChangeEventBroker

class XtextIndexMessageConsole extends MessageConsole {
    
    extension XtextIndexRules rules = new XtextIndexRules
    val resourceStream = newMessageStream
    val objectStream = newMessageStream
    val XtextIndexVirtualMachine app

    new(IStateChangeEventBroker source, IResourceDescriptions descriptions) {
        super("Xtext Index Console", null)
        app = XtextIndexVirtualMachine.INSTANCE
        app.initialize(source, descriptions)
        val display = ConsolePlugin.standardDisplay;
        resourceStream.color = display.getSystemColor(SWT.COLOR_BLACK);
        objectStream.color = display.getSystemColor(SWT.COLOR_RED);
        app.registerRule(resourceLoggerRule, resourceLoggerRule.createEmptyFilter)
        app.registerRule(objectLoggerRule, objectLoggerRule.createEmptyFilter)
        app.fire
    }

    val resourceLoggerRule = {
        resourceRule.action(XtextIndexActivationState.APPEARED) [
                resourceStream.println("Resource appeared: " + it.URI)
                resourceStream.flush
            ].action(XtextIndexActivationState.UPDATED) [
                resourceStream.println("Resource updated: " + it.URI)
                resourceStream.flush
            ].action(XtextIndexActivationState.DISAPPEARED) [
                resourceStream.println("Resource disappeared: " + it.URI)
                resourceStream.flush
            ].build
    }
    
    val objectLoggerRule = {
        objectRule.action(XtextIndexActivationState.APPEARED) [resource, object |
                objectStream.println('''«object.EClass.name» «object.name» appeared in «resource.URI»''')
                objectStream.flush
            ].action(XtextIndexActivationState.UPDATED) [resource, object |
                objectStream.println('''«object.EClass.name» «object.name» appeared in «resource.URI»''')
                objectStream.flush
            ].action(XtextIndexActivationState.DISAPPEARED) [resource, object |
                objectStream.println('''«object.EClass.name» «object.name» appeared in «resource.URI»''')
                objectStream.flush
            ].build
    }

    override protected void dispose() {
        app.unregisterRule(resourceLoggerRule, resourceLoggerRule.createEmptyFilter)
        app.unregisterRule(objectLoggerRule, objectLoggerRule.createEmptyFilter)
        super.dispose()
    }
}
