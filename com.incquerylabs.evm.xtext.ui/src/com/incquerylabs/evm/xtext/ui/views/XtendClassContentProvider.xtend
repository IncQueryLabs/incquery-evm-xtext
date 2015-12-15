package com.incquerylabs.evm.xtext.ui.views

import com.google.common.base.Preconditions
import com.incquerylabs.evm.xtext.XtextIndexRules
import org.eclipse.emf.ecore.EClass
import org.eclipse.incquery.runtime.evm.api.RuleEngine
import org.eclipse.jface.viewers.IStructuredContentProvider
import org.eclipse.jface.viewers.Viewer
import com.incquerylabs.evm.xtext.XtextIndexActivationState
import org.eclipse.incquery.runtime.evm.api.RuleSpecification
import com.incquerylabs.evm.xtext.eobject.XtextIndexedObject
import org.eclipse.incquery.runtime.evm.api.event.EventFilter
import org.eclipse.jface.viewers.TableViewer
import com.incquerylabs.evm.xtext.ui.XtextIndexVirtualMachine

class XtendClassContentProvider implements IStructuredContentProvider {
    
    extension XtextIndexRules rules = new XtextIndexRules
    
    val RuleEngine engine = XtextIndexVirtualMachine.INSTANCE.getEngine()
    
    var TableViewer viewer
    var EClass clazz
    var RuleSpecification<XtextIndexedObject> rule
    var EventFilter<XtextIndexedObject> filter 
    

    override void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
        Preconditions.checkArgument(viewer instanceof TableViewer)
        this.viewer = viewer as TableViewer
        if (oldInput instanceof EClass) {
            clazz = null
            engine.removeRule(rule, filter)
            rule = null
            filter = null
        }
        if (newInput instanceof EClass) {
            clazz = newInput as EClass
            rule = buildRule
            filter = [clazz.isSuperTypeOf(it.type)]
            engine.addRule(rule, filter)
        } else if (newInput !== null) {
            throw new IllegalArgumentException(
                String.format("Invalid input type %s for List Viewer.", newInput.getClass().getName()))
        }
    }

    override Object[] getElements(Object inputElement) {
        if (rule == null) {
            #{}
        } else {
            engine.getActivations(rule, filter).map[it.atom]            
        }
    }

    private def RuleSpecification<XtextIndexedObject> buildRule() {
        objectRule.action(XtextIndexActivationState.APPEARED)[ resource, object |
                viewer.control.display.syncExec[
                    this.viewer.add(object);
                ]
            ].action(XtextIndexActivationState.DISAPPEARED)[ resource, object |
                viewer.control.display.syncExec[
                    this.viewer.remove(object);
                ]
            ].build
    }

    override void dispose() {
         if (rule != null) {
            engine.removeRule(rule);
            rule = null;
         }
    }
}
